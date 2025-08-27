package com.crediya.loans.infrastructure.driven.persistence;

import com.crediya.loans.domain.model.LoanApplication;
import com.crediya.loans.domain.ports.out.LoanApplicationRepositoryPort;
import com.crediya.loans.infrastructure.driven.persistence.mapper.LoanApplicationMapper;
import com.crediya.loans.infrastructure.driven.persistence.repository.LoanApplicationDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * The driven adapter for loan application persistence.
 */
@Repository
@RequiredArgsConstructor
public class LoanApplicationRepositoryAdapter implements LoanApplicationRepositoryPort {

    private final LoanApplicationDataRepository loanApplicationDataRepository;
    private final LoanApplicationMapper loanApplicationMapper;

    /**
     * Saves a loan application by converting the domain model to a persistence
     * entity, saving it, and then converting the result back to a domain model.
     *
     * @param loanApplication The {@link LoanApplication} domain model to be saved.
     * @return A {@link Mono} that emits the saved {@link LoanApplication}, now with a database-generated ID.
     */
    @Override
    public Mono<LoanApplication> save(LoanApplication loanApplication) {

        var dataToSave = loanApplicationMapper.toData(loanApplication);

        return loanApplicationDataRepository.save(dataToSave)
                .map(loanApplicationMapper::toDomain);
    }
}
