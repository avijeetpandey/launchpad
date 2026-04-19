package com.avijeet.launchpad.infrastructure.persistance;

import com.avijeet.launchpad.domain.model.ConfigMaster;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfigRepository {
    ConfigMaster save(ConfigMaster config);
    Optional<ConfigMaster> findByConfigKey(String configKey);
    List<ConfigMaster> findAll();
    boolean existsByConfigKey(String configKey);
    void deleteById(UUID id);
}
