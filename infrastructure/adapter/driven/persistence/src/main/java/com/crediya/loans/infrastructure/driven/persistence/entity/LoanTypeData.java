package com.crediya.loans.infrastructure.driven.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * Represents the 'tipo_prestamo' table in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tipo_prestamo")
public class LoanTypeData {

    @Id
    @Column("id_tipo_prestamo")
    private Long id;

    @Column("nombre")
    private String name;

    @Column("monto_minimo")
    private BigDecimal minAmount;

    @Column("monto_maximo")
    private BigDecimal maxAmount;

    @Column("tasa_interes")
    private BigDecimal interestRate;

    @Column("validacion_automatica")
    private boolean automaticValidation;
}
