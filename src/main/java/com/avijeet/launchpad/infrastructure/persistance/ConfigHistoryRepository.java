package com.avijeet.launchpad.infrastructure.persistance;

import com.avijeet.launchpad.domain.model.ConfigHistory;

import java.util.List;
import java.util.UUID;

public interface ConfigHistoryRepository {
    ConfigHistory save(ConfigHistory history);
    long countByConfigId(UUID configId);
    List<ConfigHistory> findTop3ByConfigIdOrderByModifiedAtDesc(UUID configId);
    void deleteOldestByConfigId(UUID configId);
}
