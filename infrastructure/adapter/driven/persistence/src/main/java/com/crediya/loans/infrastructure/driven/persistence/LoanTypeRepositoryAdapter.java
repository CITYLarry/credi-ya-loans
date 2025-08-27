package com.crediya.loans.infrastructure.driven.persistence;

import com.crediya.loans.domain.model.LoanType;
import com.crediya.loans.domain.ports.out.LoanTypeRepositoryPort;
import com.crediya.loans.infrastructure.driven.persistence.mapper.LoanTypeMapper;
import com.crediya.loans.infrastructure.driven.persistence.repository.LoanTypeDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * This is the driven adapter that implements the LoanTypeRepositoryPort outbound port.
 */
@Repository
@RequiredArgsConstructor
public class LoanTypeRepositoryAdapter implements LoanTypeRepositoryPort {

    private final LoanTypeDataRepository loanTypeDataRepository;
    private final LoanTypeMapper loanTypeMapper;

    /**
     * Finds a loan type by its ID by calling the Spring Data repository and then
     * mapping the resulting LoanTypeData entity back to a LoanType domain model.
     *
     * @param id The unique identifier of the loan type.
     * @return A {@link Mono} emitting the found {@link LoanType} domain model.
     */
    @Override
    public Mono<LoanType> findById(Long id) {
        return loanTypeDataRepository.findById(id)
                .map(loanTypeMapper::toDomain);
    }
}
