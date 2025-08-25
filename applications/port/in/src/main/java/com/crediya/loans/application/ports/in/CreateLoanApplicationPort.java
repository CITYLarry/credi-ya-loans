package com.crediya.loans.application.ports.in;

import com.crediya.loans.domain.model.LoanApplication;
import reactor.core.publisher.Mono;

/**
 * Inbound port for the use case of creating a loan application.
 */
public interface CreateLoanApplicationPort {

    /**
     * Executes the use case to create a new loan application.
     *
     * @param command A {@link CreateLoanApplicationCommand} object containing all necessary data for the operation.
     * @return A {@link Mono} that emits the newly created {@link LoanApplication}.
     */
    Mono<LoanApplication> createLoanApplication(CreateLoanApplicationCommand command);
}
