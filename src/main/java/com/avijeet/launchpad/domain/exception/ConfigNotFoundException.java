package com.avijeet.launchpad.domain.exception;

public class ConfigNotFoundException extends LaunchpadException {
    public ConfigNotFoundException(String configKey) {
        super("Config not found for key: " + configKey);
    }
}
