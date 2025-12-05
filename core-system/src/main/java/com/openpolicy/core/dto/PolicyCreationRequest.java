package com.openpolicy.core.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PolicyCreationRequest {
    // SIN ID (porque es nuevo)
    // SIN STATUS (porque lo define el sistema)

    @NotBlank(message = "El número de póliza es obligatorio")
    @Pattern(regexp = "^POL-\\d{3,5}$|^POL-NEW-\\d{3}$", message = "El formato debe ser POL-XXXX")
    private String policyNumber;

    @NotBlank(message = "El nombre del titular no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String holderName;

    @NotNull(message = "La prima es obligatoria")
    @Positive(message = "El monto de la prima debe ser positivo")
    private BigDecimal premiumAmount;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La póliza no puede iniciar en el pasado")
    private LocalDate startDate;
}