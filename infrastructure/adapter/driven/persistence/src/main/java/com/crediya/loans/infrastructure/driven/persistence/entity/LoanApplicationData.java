package com.crediya.loans.infrastructure.driven.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * Represents the 'solicitud' table in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("solicitud")
public class LoanApplicationData {

    @Id
    @Column("id_solicitud")
    private Long id;

    @Column("monto")
    private BigDecimal amount;

    @Column("plazo")
    private Integer term;

    @Column("email")
    private String customerEmail;

    @Column("id_estado")
    private Long statusId;

    @Column("id_tipo_prestamo")
    private Long loanTypeId;
}
