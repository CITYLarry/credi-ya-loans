package com.crediya.loans.infrastructure.driven.persistence.mapper;

import com.crediya.loans.domain.model.Status;
import com.crediya.loans.infrastructure.driven.persistence.entity.StatusData;
import org.mapstruct.Mapper;

/**
 * A MapStruct mapper for converting between the Status domain model and the StatusData persistence entity.
 *
 * @Mapper(componentModel = "spring") tells MapStruct to generate an implementation
 * that is a Spring component, which can then be injected into the repository adapters.
 */
@Mapper(componentModel = "spring")
public interface StatusMapper {

    /**
     * Maps a StatusData entity to a Status domain model.
     *
     * @param statusData The persistence entity.
     * @return The corresponding Status domain model.
     */
    Status toDomain(StatusData statusData);

    /**
     * Maps a Status domain model to a StatusData entity.
     *
     * @param status The domain model.
     * @return The corresponding StatusData persistence entity.
     */
    StatusData toData(Status status);
}
