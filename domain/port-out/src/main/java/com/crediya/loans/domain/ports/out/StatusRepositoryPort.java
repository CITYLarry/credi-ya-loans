package com.crediya.loans.domain.ports.out;

import com.crediya.loans.domain.model.Status;
import reactor.core.publisher.Mono;

/**
 * Outbound port for status persistence.
 */
public interface StatusRepositoryPort {

    /**
     * Finds a status by its business name.
     *
     * @param name The unique business name of the status.
     * @return A {@link Mono} emitting the found {@link Status}, or an empty Mono if not found.
     */
    Mono<Status> findByName(String name);
}
