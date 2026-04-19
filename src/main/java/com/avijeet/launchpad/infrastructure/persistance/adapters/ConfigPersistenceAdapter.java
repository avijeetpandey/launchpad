package com.avijeet.launchpad.infrastructure.persistance.adapters;

import com.avijeet.launchpad.domain.model.ConfigMaster;
import com.avijeet.launchpad.infrastructure.persistance.ConfigRepository;
import com.avijeet.launchpad.infrastructure.persistance.JpaConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConfigPersistenceAdapter implements ConfigRepository {

    private final JpaConfigRepository jpaConfigRepository;

    @Override
    public ConfigMaster save(ConfigMaster config) {
       return jpaConfigRepository.save(config);
    }

    @Override
    public Optional<ConfigMaster> findByConfigKey(String configKey) {
        return jpaConfigRepository.findByConfigKey(configKey);
    }

    @Override
    public List<ConfigMaster> findAll() {
        return jpaConfigRepository.findAll();
    }

    @Override
    public boolean existsByConfigKey(String configKey) {
        return jpaConfigRepository.existsByConfigKey(configKey);
    }

    @Override
    public void deleteById(UUID id) {

    }
}
