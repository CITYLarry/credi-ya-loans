package com.crediya.loans.domain.model;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Represents a customer's loan application.
 */
public class LoanApplication {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    private final Long id;
    private final BigDecimal amount;
    private final Integer term;
    private final String customerEmail;
    private final Long statusId;
    private final Long loanTypeId;

    public LoanApplication(Long id, BigDecimal amount, Integer term, String customerEmail, Long statusId, Long loanTypeId) {
        validateAmount(amount);
        validateTerm(term);
        validateCustomerEmail(customerEmail);
        validateIds(statusId, loanTypeId);

        this.id = id;
        this.amount = amount;
        this.term = term;
        this.customerEmail = customerEmail;
        this.statusId = statusId;
        this.loanTypeId = loanTypeId;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be a positive value.");
        }
    }

    private void validateTerm(Integer term) {
        if (term == null || term <= 0) {
            throw new IllegalArgumentException("Loan term must be a positive number of months.");
        }
    }

    private void validateCustomerEmail(String customerEmail) {
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty.");
        }
        if (!EMAIL_PATTERN.matcher(customerEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private void validateIds(Long statusId, Long loanTypeId) {
        if (statusId == null) {
            throw new IllegalArgumentException("Status ID cannot be null.");
        }
        if (loanTypeId == null) {
            throw new IllegalArgumentException("Loan Type ID cannot be null.");
        }
    }


    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getTerm() {
        return term;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Long getStatusId() {
        return statusId;
    }

    public Long getLoanTypeId() {
        return loanTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanApplication that = (LoanApplication) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(amount, that.amount) &&
                java.util.Objects.equals(term, that.term) &&
                java.util.Objects.equals(customerEmail, that.customerEmail) &&
                java.util.Objects.equals(statusId, that.statusId) &&
                java.util.Objects.equals(loanTypeId, that.loanTypeId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, amount, term, customerEmail, statusId, loanTypeId);
    }
}
