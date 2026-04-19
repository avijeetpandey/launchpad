package com.avijeet.launchpad.application.event;

import com.avijeet.launchpad.domain.model.ConfigHistory;
import com.avijeet.launchpad.infrastructure.persistance.ConfigHistoryRepository;
import com.flipkart.zjsonpatch.JsonDiff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfigAuditListener {
    private final ConfigHistoryRepository historyRepository;

    @Async
    @EventListener
    @Transactional
    public void handleConfigUpdatedEvent(ConfigUpdatedEvent event) {
        log.debug("Generating audit trail for config: {}", event.configId());

        var diff = JsonDiff.asJson(event.previousPayload(), event.currentPayload());

        ConfigHistory history = ConfigHistory.builder()
                .configId(event.configId())
                .previousPayload(event.previousPayload())
                .diffSummary(diff)
                .modifiedBy(event.modifiedBy())
                .build();

        historyRepository.save(history);

        if (historyRepository.countByConfigId(event.configId()) > 3) {
            historyRepository.deleteOldestByConfigId(event.configId());
        }
    }
}
