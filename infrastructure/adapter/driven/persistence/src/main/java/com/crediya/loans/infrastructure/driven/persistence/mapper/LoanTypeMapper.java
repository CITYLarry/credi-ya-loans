package com.crediya.loans.infrastructure.driven.persistence.mapper;

import com.crediya.loans.domain.model.LoanType;
import com.crediya.loans.infrastructure.driven.persistence.entity.LoanTypeData;
import org.mapstruct.Mapper;

/**
 * A MapStruct mapper for converting between the LoanType domain model and the LoanTypeData persistence entity.
 *
 * @Mapper(componentModel = "spring") tells MapStruct to generate an implementation
 * that is a Spring component, which can then be injected into the repository adapters.
 */
@Mapper(componentModel = "spring")
public interface LoanTypeMapper {

    /**
     * Maps a LoanTypeData entity to a LoanType domain model.
     *
     * @param loanTypeData The persistence entity.
     * @return The corresponding LoanType domain model.
     */
    LoanType toDomain(LoanTypeData loanTypeData);

    /**
     * Maps a LoanType domain model to a LoanTypeData entity.
     *
     * @param loanType The domain model.
     * @return The corresponding LoanTypeData persistence entity.
     */
    LoanTypeData toData(LoanType loanType);
}