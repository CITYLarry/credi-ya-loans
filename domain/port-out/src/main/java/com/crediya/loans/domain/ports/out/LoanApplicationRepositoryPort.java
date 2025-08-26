package com.crediya.loans.domain.ports.out;

import com.crediya.loans.domain.model.LoanApplication;
import reactor.core.publisher.Mono;

/**
 * Outbound port for loan application persistence.
 */
public interface LoanApplicationRepositoryPort {

    /**
     * Saves a new loan application.
     *
     * @param loanApplication The {@link LoanApplication} object to be saved.
     * @return A {@link Mono} that emits the saved {@link LoanApplication}.
     */
    Mono<LoanApplication> save(LoanApplication loanApplication);
}
