package com.crediya.loans.infrastructure.driven.persistence.repository;

import com.crediya.loans.infrastructure.driven.persistence.entity.LoanTypeData;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data R2DBC repository interface for the LoanTypeData entity.
 *
 * @Repository marks this interface as a Spring component to be managed by the IoC container.
 */
@Repository
public interface LoanTypeDataRepository extends R2dbcRepository<LoanTypeData, Long> {

}
