package com.openpolicy.core.model;

import jakarta.persistence.*; // Importante: Todo JPA viene de aquí
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // 1. Marca esto como una tabla de BD
@Table(name = "policies") // 2. (Opcional) Define el nombre exacto de la tabla
public class Policy {

    @Id // 3. Marca la llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Auto-incremental (como AUTO_INCREMENT)
    private Long id;

    @Column(nullable = false, unique = true) // Configuración extra de columnas
    private String policyNumber;

    @Column(nullable = false)
    private String holderName;

    private BigDecimal premiumAmount;
    private LocalDate startDate;
    private String status;
    // RELACIÓN: Una Póliza tiene Muchos Siniestros
    // mappedBy = "policy" -> Se refiere al campo 'policy' en la clase Claim
    // cascade = ALL -> Si borro la póliza, borra sus siniestros
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims = new ArrayList<>();
}