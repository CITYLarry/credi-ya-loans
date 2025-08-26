package com.crediya.loans.application.exceptions;

/**
 * A custom business exception thrown when a required status is not found in the system.
 */
public class StatusNotFoundException extends RuntimeException {
    public StatusNotFoundException(String message) {
        super(message);
    }
}
