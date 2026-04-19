package com.avijeet.launchpad.presentation.handler;


import com.avijeet.launchpad.domain.exception.ConfigNotFoundException;
import com.avijeet.launchpad.domain.exception.InvalidConfigPayloadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConfigNotFoundException.class)
    public ProblemDetail handleNotFound(ConfigNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("http://localhost/errors/config-not-found"));
        problemDetail.setTitle("Configuration Not Found");
        return problemDetail;
    }

    @ExceptionHandler(InvalidConfigPayloadException.class)
    public ProblemDetail handleInvalidPayload(InvalidConfigPayloadException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("http://localhost/errors/invalid-payload"));
        problemDetail.setTitle("Invalid Configuration Payload");
        return problemDetail;
    }
}