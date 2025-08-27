package com.crediya.loans.infrastructure.entrypoints.web.dto;

import com.crediya.loans.application.ports.in.CreateLoanApplicationCommand;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for the loan application request.
 */
@Data
@Builder
public class LoanApplicationRequest {

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Term cannot be null")
    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer term;

    @NotBlank(message = "Customer email cannot be blank")
    @Email(message = "Must be a valid email format")
    private String customerEmail;

    @NotNull(message = "Loan type ID cannot be null")
    private Long loanTypeId;

    /**
     * Maps this web DTO to the application layer's command object.
     *
     * @return A {@link CreateLoanApplicationCommand} object.
     */
    public CreateLoanApplicationCommand toCommand() {
        return new CreateLoanApplicationCommand(
                this.amount,
                this.term,
                this.customerEmail,
                this.loanTypeId
        );
    }
}
