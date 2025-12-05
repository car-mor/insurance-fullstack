package com.openpolicy.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Policy {
    // Quitamos @Id porque JdbcTemplate no lo usa (lo manejamos manual en el SQL)
    private Long id;
    private String policyNumber;
    private String holderName;
    private BigDecimal premiumAmount;
    private LocalDate startDate;
    private String status;
}