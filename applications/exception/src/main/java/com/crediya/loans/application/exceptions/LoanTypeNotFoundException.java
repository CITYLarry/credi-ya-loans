package com.crediya.loans.application.exceptions;

/**
 * A custom business exception thrown when a loan application is submitted
 * for a loan type that does not exist in the system.
 */
public class LoanTypeNotFoundException extends RuntimeException {
    public LoanTypeNotFoundException(String message) {
        super(message);
    }
}
