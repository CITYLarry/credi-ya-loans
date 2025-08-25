package com.crediya.loans.domain.model;

import java.math.BigDecimal;

/**
 * Represents a type of loan offered by CrediYa.
 */
public class LoanType {

    private final Long id;
    private final String name;
    private final BigDecimal minAmount;
    private final BigDecimal maxAmount;
    private final BigDecimal interestRate;
    private final boolean automaticValidation;


    public LoanType(Long id,
                    String name,
                    BigDecimal minAmount,
                    BigDecimal maxAmount,
                    BigDecimal interestRate,
                    boolean automaticValidation) {

        validateName(name);
        validateAmounts(minAmount, maxAmount);
        validateInterestRate(interestRate);

        this.id = id;
        this.name = name;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.interestRate = interestRate;
        this.automaticValidation = automaticValidation;
    }

    private void validateAmounts(BigDecimal minAmount, BigDecimal maxAmount) {
        if (minAmount == null || minAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Minimum amount must be a positive value.");
        }
        if (maxAmount == null || maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maximum amount must be a positive value.");
        }
        if (minAmount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("Minimum amount cannot be greater than the maximum amount.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Loan type name cannot be null or empty.");
        }
    }

    private void validateInterestRate(BigDecimal interestRate) {
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public boolean isAutomaticValidation() {
        return automaticValidation;
    }
}
