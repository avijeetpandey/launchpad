package com.avijeet.launchpad.presentation.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record ConfigRequest(
    JsonNode payload,
    String apiEndpoint,
    String status
) { }
