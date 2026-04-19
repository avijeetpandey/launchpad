package com.avijeet.launchpad.application.validation;

import com.avijeet.launchpad.domain.exception.InvalidConfigPayloadException;
import tools.jackson.databind.JsonNode;

public interface JsonValidatorStrategy {
    void validate(JsonNode payload) throws InvalidConfigPayloadException;
}
