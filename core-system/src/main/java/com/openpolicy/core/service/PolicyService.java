package com.openpolicy.core.service;

import com.openpolicy.core.dto.PolicyCreationRequest;
import com.openpolicy.core.dto.PolicyResponse;
import com.openpolicy.core.model.Policy;
import com.openpolicy.core.repository.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Transactional //Todo o nada
    // CAMBIO CLAVE: Recibe Request, devuelve Response
    public PolicyResponse createPolicy(PolicyCreationRequest request) {

        // 1. Convertir Request -> Entidad
        Policy policyEntity = new Policy();
        policyEntity.setPolicyNumber(request.getPolicyNumber());
        policyEntity.setHolderName(request.getHolderName());
        policyEntity.setPremiumAmount(request.getPremiumAmount());
        policyEntity.setStartDate(request.getStartDate());

        // Lógica de negocio (Status)
        policyEntity.setStatus("PENDING");

        // 2. Guardar (El repo sigue usando Entidad)
        Policy savedPolicy = policyRepository.save(policyEntity);

        // 3. Convertir Entidad -> Response
        return mapToResponse(savedPolicy);
    }

    // También actualizamos el GET para devolver Responses
    public List<PolicyResponse> getAllPolicies() {
        return policyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Mapper auxiliar
    private PolicyResponse mapToResponse(Policy entity) {
        PolicyResponse response = new PolicyResponse();
        response.setId(entity.getId());
        response.setPolicyNumber(entity.getPolicyNumber());
        response.setHolderName(entity.getHolderName());
        response.setPremiumAmount(entity.getPremiumAmount());
        response.setStartDate(entity.getStartDate());
        response.setStatus(entity.getStatus());
        return response;
    }
}