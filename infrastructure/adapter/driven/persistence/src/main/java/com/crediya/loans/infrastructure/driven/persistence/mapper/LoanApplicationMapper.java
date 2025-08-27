package com.crediya.loans.infrastructure.driven.persistence.mapper;

import com.crediya.loans.domain.model.LoanApplication;
import com.crediya.loans.infrastructure.driven.persistence.entity.LoanApplicationData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * A MapStruct mapper for converting between the LoanApplication domain model (the aggregate root)
 * and the LoanApplicationData persistence entity.
 *
 * @Mapper(componentModel = "spring") tells MapStruct to generate an implementation
 * that is a Spring component, which can then be injected into the repository adapters.
 */
@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    /**
     * Maps a LoanApplicationData entity to a LoanApplication domain model.
     *
     * @param loanApplicationData The persistence entity.
     * @return The corresponding LoanApplication domain model.
     */
    LoanApplication toDomain(LoanApplicationData loanApplicationData);

    /**
     * Maps a LoanApplication domain model to a LoanApplicationData entity.
     * The 'id' field is ignored in this mapping because for new applications, it will be null,
     * and the database is responsible for generating it upon insertion.
     *
     * @param loanApplication The domain model.
     * @return The corresponding LoanApplicationData persistence entity.
     */
    @Mapping(target = "id", ignore = true)
    LoanApplicationData toData(LoanApplication loanApplication);
}
