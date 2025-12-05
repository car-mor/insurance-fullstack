package com.openpolicy.core.service;

//import com.openpolicy.core.model.Policy;
import com.openpolicy.core.dto.PolicyDto;
import com.openpolicy.core.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // Habilita el uso de Mocks
class PolicyServiceTest {

    @Mock // Creamos un repositorio "falso" (simulado)
    private PolicyRepository policyRepository;

    @InjectMocks // Inyectamos el repositorio falso dentro del servicio real
    private PolicyServiceDto policyService;

    @Test
    void createPolicy_ShouldSetStatusToPending_WhenStatusIsNull() {
//        // 1. GIVEN (Preparamos los datos)
//        PolicyDto policyInput = new PolicyDto(null, "POL-TEST", "Juan Test", new BigDecimal("100"), LocalDate.now(), null);
//
//        // Configuramos el simulacro: "Cuando llames a save(), devuelve la misma póliza pero con ID 1"
//        // Esto evita ir a la base de datos real.
//        when(policyRepository.save(any(PolicyDto.class))).thenAnswer(invocation -> {
//            PolicyDto p = invocation.getArgument(0);
//            p.setId(1L);
//            return p;
//        });
//
//        // 2. WHEN (Ejecutamos la acción real)
//        PolicyDto result = policyService.createPolicy(policyInput);
//
//        // 3. THEN (Verificamos el resultado)
//        // La regla de negocio decía: si es null, ponle PENDING. ¿Lo hizo?
//        assertEquals("PENDING", result.getStatus());
//
//        // Verificamos que el servicio sí llamó al repositorio una vez
//        verify(policyRepository).save(any(PolicyDto.class));

//        / Creamos un DTO en lugar de una Entidad
        PolicyDto newPolicyDto = new PolicyDto();
        newPolicyDto.setPolicyNumber("POL-NEW-999");
        newPolicyDto.setHolderName("Maria Service");
        newPolicyDto.setPremiumAmount(new BigDecimal("3000.00"));
        newPolicyDto.setStartDate(LocalDate.now());
        // No seteamos status, el servicio lo pondrá en PENDING automáticamente

        // Ahora sí, pasamos el DTO al servicio
        PolicyDto savedPolicy = policyService.createPolicyDto(newPolicyDto);

        System.out.println("¡Éxito! Póliza creada. ID: " + savedPolicy.getId());

        System.out.println("\n--- LISTA ACTUALIZADA ---");
        policyService.getAllPolicies().forEach(System.out::println);
    }
}