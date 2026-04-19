package com.avijeet.launchpad.infrastructure.persistance;

import com.avijeet.launchpad.domain.model.ConfigHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaConfigHistoryRepository extends JpaRepository<ConfigHistory, UUID> {
   long countByConfigId(UUID configId);
   List<ConfigHistory> findTop3ByConfigIdOrderByModifiedAtDesc(UUID configId);

   @Modifying
   @Query(value = "DELETE FROM config_history WHERE id IN " + "SELECT id FROM config_history where config_id = :configId ORDER BY modified_at ASC LIMIT 1)", nativeQuery = true)
   void deleteOldestByConfigId(UUID configId);
}
