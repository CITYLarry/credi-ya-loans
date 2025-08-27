package com.crediya.loans.infrastructure.driven.persistence.repository;

import com.crediya.loans.infrastructure.driven.persistence.entity.LoanApplicationData;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data R2DBC repository interface for the LoanApplicationData entity.
 *
 * @Repository marks this interface as a Spring component to be managed by the IoC container.
 */
@Repository
public interface LoanApplicationDataRepository extends R2dbcRepository<LoanApplicationData, Long> {

}
