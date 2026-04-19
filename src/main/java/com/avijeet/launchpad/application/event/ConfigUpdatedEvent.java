package com.avijeet.launchpad.application.event;

import lombok.Builder;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Builder
public record ConfigUpdatedEvent (
        UUID configId,
        JsonNode previousPayload,
        JsonNode currentPayload,
        String modifiedBy
) {}
