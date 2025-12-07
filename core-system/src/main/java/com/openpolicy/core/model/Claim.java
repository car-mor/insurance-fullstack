package com.openpolicy.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String claimNumber; // Ej: CL-001
    private String description; // Ej: "Choque en Avenida"
    private LocalDate incidentDate;
    private BigDecimal estimatedCost;

    // RELACIÓN: Muchos Siniestros pertenecen a Una Póliza
    @ManyToOne(fetch = FetchType.LAZY) // Lazy = No traigas la póliza completa a menos que te la pida
    @JoinColumn(name = "policy_id") // Nombre de la columna en la tabla 'claims'
    private Policy policy;
}