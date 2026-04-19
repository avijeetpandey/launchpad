package com.avijeet.launchpad.infrastructure.persistance;

import com.avijeet.launchpad.domain.model.ConfigMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaConfigRepository extends JpaRepository<ConfigMaster, UUID> {
    Optional<ConfigMaster> findByConfigKey(String configKey);
    boolean existsByConfigKey(String configKey);
}
