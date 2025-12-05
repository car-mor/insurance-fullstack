package com.openpolicy.core.service;

import com.openpolicy.core.dto.PolicyDto;
import com.openpolicy.core.model.Policy; // Importamos la Entidad
import com.openpolicy.core.repository.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//@Service
public class PolicyServiceDto {

    private final PolicyRepository policyRepository;

    public PolicyServiceDto(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }
    //Pasa todo o nada el decorador transactional
    @Transactional
    public PolicyDto createPolicyDto(PolicyDto policyDtoInput) {
        // 1. MAPEO: Convertir DTO (Input) -> Entidad (Para BD)
        // --- FASE 1: TRADUCCIÓN (Mapping) ---
        // Convertimos el (DTO) que pasa Controller en campos del modelo (Entidad)
        Policy policyEntity = new Policy();
        // ... copiamos los datos, menos id y status, por ser campos sensibles del input
        //Idealmente se tiene que crear un PolicyCreationRequest.java (Solo campos de entrada + Validaciones)
        //Y un PolicyResponse.java (Todos los campos, sin validaciones)
        //El Controller recibiría el primero, y devolvería el segundo
        policyEntity.setPolicyNumber(policyDtoInput.getPolicyNumber());
        policyEntity.setHolderName(policyDtoInput.getHolderName());
        policyEntity.setPremiumAmount(policyDtoInput.getPremiumAmount());
        policyEntity.setStartDate(policyDtoInput.getStartDate());
        // --- FASE 2: REGLAS DE NEGOCIO ---
        // React no envió el status. Nosotros (el negocio) decidimos que nace PENDING.
        // La lógica de negocio se aplica sobre la ENTIDAD
        policyEntity.setStatus("PENDING");
        // --- FASE 3: PERSISTENCIA ---
        // Llamamos al almacén: Al repo le damos la ENTIDAD, no el DTO.
        // 2. GUARDAR: El repositorio recibe la Entidad, no el DTO
        Policy savedPolicy = policyRepository.save(policyEntity);
        // --- FASE 4: RESPUESTA ---
        // Convertimos la Entidad (ya con ID generado) de vuelta a DTO para mostrársela a React.
        // 3. MAPEO INVERSO: Convertir la Entidad guardada -> DTO (Output)
        // (Aquí devolvemos los datos para que el Controller responda)
        return mapToDto(savedPolicy);
    }

    public List<PolicyDto> getAllPolicies() {
        // El repo devuelve Entidades (List<Policy>)
        List<Policy> entities = policyRepository.findAll();

        // Convertimos la lista de Entidades a lista de DTOs usando streams
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Método auxiliar para no repetir código de conversión
    private PolicyDto mapToDto(Policy entity) {
        PolicyDto dto = new PolicyDto();
        // OJO: Si tu PolicyDto NO tiene setId o setStatus, no podrás devolverlos aquí.
        // Normalmente el DTO de respuesta SÍ debería tenerlos (sin validaciones).
        // Por ahora mapeamos lo que tenemos:
        dto.setPolicyNumber(entity.getPolicyNumber());
        dto.setHolderName(entity.getHolderName());
        dto.setPremiumAmount(entity.getPremiumAmount());
        dto.setStartDate(entity.getStartDate());
        return dto;
    }
}