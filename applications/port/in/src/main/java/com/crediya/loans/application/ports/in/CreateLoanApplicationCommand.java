package com.crediya.loans.application.ports.in;

import java.math.BigDecimal;

/**
 * Represents a command to create a new loan application using a Java Record.
 */
public record CreateLoanApplicationCommand(
        BigDecimal amount,
        Integer term,
        String customerEmail,
        Long loanTypeId
) {
    public CreateLoanApplicationCommand {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be a positive value.");
        }
        if (term == null || term <= 0) {
            throw new IllegalArgumentException("Term must be a positive integer.");
        }
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty.");
        }
        if (loanTypeId == null) {
            throw new IllegalArgumentException("Loan Type ID cannot be null.");
        }
    }
}
