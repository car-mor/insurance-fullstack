package com.openpolicy.core.controller;

//import com.openpolicy.core.model.Policy;
import com.openpolicy.core.dto.PolicyDto;
import com.openpolicy.core.service.PolicyServiceDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController // Indica que esta clase responde con datos (JSON)
//@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyControllerDto {

    private final PolicyServiceDto policyService;

    public PolicyControllerDto(PolicyServiceDto policyService) {
        this.policyService = policyService;
    }

    // GET: Obtener todas las pólizas
    @GetMapping
    public List<PolicyDto> getAllPolicies() {
        return policyService.getAllPolicies();
    }

    // POST: Crear una póliza
    // @Valid activa las anotaciones que pusimos en PolicyDto.java (@NotBlank, @Positive, etc.)
    // @Valid: "Revisa que no tenga errores antes de pasarla a Service"
    // @RequestBody: "Toma el JSON que viene de internet y conviértelo a Java"
    @PostMapping
    public PolicyDto createPolicy(@Valid @RequestBody PolicyDto policy) {
        return policyService.createPolicyDto(policy);
    }
}