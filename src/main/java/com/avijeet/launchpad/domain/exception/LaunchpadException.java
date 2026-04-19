package com.avijeet.launchpad.domain.exception;

public abstract class LaunchpadException extends RuntimeException {
    public LaunchpadException(String message) {
        super(message);
    }

    public LaunchpadException(String message, Throwable cause) {
        super(message, cause);
    }
}
