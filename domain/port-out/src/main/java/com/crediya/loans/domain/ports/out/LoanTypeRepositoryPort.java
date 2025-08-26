package com.crediya.loans.domain.ports.out;

import com.crediya.loans.domain.model.LoanType;
import reactor.core.publisher.Mono;

/**
 * Outbound port for loan type persistence.
 */
public interface LoanTypeRepositoryPort {

    /**
     * Finds a loan type by its unique identifier.
     *
     * @param id The unique identifier of the loan type.
     * @return A {@link Mono} emitting the found {@link LoanType}, or an empty Mono if not found.
     */
    Mono<LoanType> findById(Long id);
}
