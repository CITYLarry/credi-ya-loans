package com.crediya.loans.infrastructure.driven.persistence;

import com.crediya.loans.domain.model.Status;
import com.crediya.loans.domain.ports.out.StatusRepositoryPort;
import com.crediya.loans.infrastructure.driven.persistence.mapper.StatusMapper;
import com.crediya.loans.infrastructure.driven.persistence.repository.StatusDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * This is the driven adapter that implements the StatusRepositoryPort outbound port.
 */
@Repository
@RequiredArgsConstructor
public class StatusRepositoryAdapter implements StatusRepositoryPort {

    private final StatusDataRepository statusDataRepository;
    private final StatusMapper statusMapper;

    /**
     * Finds a status by its name by calling the Spring Data repository and
     * then mapping the result from a persistence entity to a domain model.
     *
     * @param name The unique business name of the status.
     * @return A {@link Mono} emitting the found {@link Status} domain model.
     */
    @Override
    public Mono<Status> findByName(String name) {
        return statusDataRepository.findByName(name)
                .map(statusMapper::toDomain);
    }
}
