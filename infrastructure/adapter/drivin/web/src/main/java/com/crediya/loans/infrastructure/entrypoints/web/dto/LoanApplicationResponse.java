package com.crediya.loans.infrastructure.entrypoints.web.dto;

import com.crediya.loans.domain.model.LoanApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for the loan application response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponse {

    private Long applicationId;
    private String customerEmail;
    private BigDecimal requestedAmount;
    private String loanTypeName;
    private String status;
    private String message;

    /**
     * A static factory method to create a response DTO from a domain User object.
     *
     * @param loanApplication The loan application domain object that was successfully saved.
     * @return A new UserRegistrationResponse object.
     */
    public static LoanApplicationResponse fromDomain(LoanApplication loanApplication) {
        return LoanApplicationResponse.builder()
                .applicationId(loanApplication.getId())
                .customerEmail(loanApplication.getCustomerEmail())
                .requestedAmount(loanApplication.getAmount())
                .loanTypeName(loanApplication.getLoanType().getName())
                .status(loanApplication.getStatus().getName())
                .message("Loan application received successfully. Awaiting review.")
                .build();
    }
}
