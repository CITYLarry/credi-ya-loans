package com.crediya.loans.infrastructure.driven.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents the 'estados' table in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("estados")
public class StatusData {

    @Id
    @Column("id_estado")
    private Long id;

    @Column("nombre")
    private String name;

    @Column("descripcion")
    private String description;
}
