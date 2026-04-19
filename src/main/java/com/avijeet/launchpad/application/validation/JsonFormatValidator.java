package com.avijeet.launchpad.application.validation;

import com.avijeet.launchpad.domain.exception.InvalidConfigPayloadException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class JsonFormatValidator implements JsonValidatorStrategy {

    @Override
    public void validate(JsonNode payload) throws InvalidConfigPayloadException {
        if(payload == null || payload.isEmpty() || payload.isNull()) {
            throw new InvalidConfigPayloadException("JSON payload is null or empty");
        }
    }
}
