package com.avijeet.launchpad.application.service;

import com.avijeet.launchpad.application.event.ConfigUpdatedEvent;
import com.avijeet.launchpad.application.validation.JsonValidatorStrategy;
import com.avijeet.launchpad.domain.exception.ConfigNotFoundException;
import com.avijeet.launchpad.domain.model.ConfigMaster;
import com.avijeet.launchpad.infrastructure.persistance.ConfigHistoryRepository;
import com.avijeet.launchpad.infrastructure.persistance.ConfigRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.flipkart.zjsonpatch.JsonDiff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigLifeCycleService {
    private final ConfigRepository configRepository;
    private final ConfigHistoryRepository configHistoryRepository;
    private final JsonValidatorStrategy jsonValidatorStrategy;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @CachePut(value = "config", key = "#configKey")
    public ConfigMaster createOrUpdate(String configKey, JsonNode payload, String endpoint, String status, String user) {
        jsonValidatorStrategy.validate(payload);

        return configRepository.findByConfigKey(configKey).map(existing -> {
            JsonNode oldPayload = existing.getCurrentPayload().deepCopy();
            existing.setCurrentPayload(payload);
            existing.setApiEndpointAssociation(endpoint);
            existing.setStatus(status);
            existing.setUpdatedBy(user);

            ConfigMaster saved = configRepository.save(existing);
            applicationEventPublisher.publishEvent(new ConfigUpdatedEvent(saved.getId(), oldPayload, payload, user));
            return saved;
        }).orElseGet(() -> {
            ConfigMaster newConfig = ConfigMaster.builder()
                    .configKey(configKey)
                    .currentPayload(payload)
                    .apiEndpointAssociation(endpoint)
                    .status(status)
                    .createdBy(user)
                    .updatedBy(user)
                    .build();
            return configRepository.save(newConfig);
        });
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "configs", key = "#configKey")
    public ConfigMaster getByKey(String configKey) {
        return configRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new ConfigNotFoundException(configKey));
    }

    @Transactional(readOnly = true)
    public List<ConfigMaster> getAllConfigs() {
        return configRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "configs", key = "#configKey")
    public void toggleStatus(String configKey, String status, String user) {
        ConfigMaster config = getByKey(configKey);
        config.setStatus(status);
        config.setUpdatedBy(user);
        configRepository.save(config);
    }

    @Transactional
    @CacheEvict(value = "configs", key = "#configKey")
    public void deleteConfig(String configKey) {
        ConfigMaster config = getByKey(configKey);
        configRepository.deleteById(config.getId());
    }

    @Transactional(readOnly = true)
    public JsonNode generateDiff(String configKey, JsonNode proposedPayload) {
        jsonValidatorStrategy.validate(proposedPayload);
        ConfigMaster currentConfig = getByKey(configKey);
        return JsonDiff.asJson(currentConfig.getCurrentPayload(), proposedPayload);
    }

    @Transactional(readOnly = true)
    public Object getHistory(String configKey) {
        ConfigMaster config = getByKey(configKey);
        return configHistoryRepository.findTop3ByConfigIdOrderByModifiedAtDesc(config.getId());
    }
}
