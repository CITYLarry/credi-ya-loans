package com.crediya.loans.infrastructure.driven.persistence.repository;

import com.crediya.loans.infrastructure.driven.persistence.entity.StatusData;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository interface for the StatusData entity.
 *
 * @Repository marks this interface as a Spring component to be managed by the IoC container.
 */
@Repository
public interface StatusDataRepository extends R2dbcRepository<StatusData, Long> {

    /**
     * Custom query method to find a status by its unique name.
     * Spring Data R2DBC will automatically generate the implementation for this method
     * based on its name ("find by name"). This is known as a derived query.
     *
     * @param name The name of the status to find.
     * @return A {@link Mono} emitting the found StatusData, or empty if not found.
     */
    Mono<StatusData> findByName(String name);
}
