package com.avijeet.launchpad.presentation.dto;

import com.avijeet.launchpad.domain.model.ConfigMaster;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public record ConfigResponse(
        String configKey,
        JsonNode payload,
        String apiEndpointAssociation,
        String status,
        String updatedBy,
        LocalDateTime updatedAt
) {
    public static ConfigResponse fromEntity(ConfigMaster entity) {
        return new ConfigResponse(
                entity.getConfigKey(),
                entity.getCurrentPayload(),
                entity.getApiEndpointAssociation(),
                entity.getStatus(),
                entity.getUpdatedBy(),
                entity.getUpdatedAt()
        );
    }
}
