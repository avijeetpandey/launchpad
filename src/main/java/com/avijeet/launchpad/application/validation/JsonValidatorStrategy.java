package com.avijeet.launchpad.application.validation;

import com.avijeet.launchpad.domain.exception.InvalidConfigPayloadException;
import com.fasterxml.jackson.databind.JsonNode;

public interface JsonValidatorStrategy {
    void validate(JsonNode payload) throws InvalidConfigPayloadException;
}
