package com.avijeet.launchpad.application.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ConfigUpdatedEvent (
        UUID configId,
        JsonNode previousPayload,
        JsonNode currentPayload,
        String modifiedBy
) {}
