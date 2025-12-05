package com.openpolicy.core.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PolicyResponse {
    // Aquí SÍ van el ID y el Status para que el Frontend los vea
    private Long id;
    private String policyNumber;
    private String holderName;
    private BigDecimal premiumAmount;
    private LocalDate startDate;
    private String status;
}