package com.avijeet.launchpad.infrastructure.persistance.adapters;

import com.avijeet.launchpad.domain.model.ConfigHistory;
import com.avijeet.launchpad.infrastructure.persistance.ConfigHistoryRepository;
import com.avijeet.launchpad.infrastructure.persistance.JpaConfigHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConfigHistoryPersistenceAdapter implements ConfigHistoryRepository {

    private final JpaConfigHistoryRepository jpaConfigHistoryRepository;

    @Override
    public ConfigHistory save(ConfigHistory history) {
        return jpaConfigHistoryRepository.save(history);
    }

    @Override
    public long countByConfigId(UUID configId) {
        return jpaConfigHistoryRepository.countByConfigId(configId);
    }

    @Override
    public List<ConfigHistory> findTop3ByConfigIdOrderByModifiedAtDesc(UUID configId) {
        return jpaConfigHistoryRepository.findTop3ByConfigIdOrderByModifiedAtDesc(configId);
    }

    @Override
    public void deleteOldestByConfigId(UUID configId) {
        jpaConfigHistoryRepository.deleteOldestByConfigId(configId);
    }
}
