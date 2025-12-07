package com.openpolicy.core.controller;

import com.openpolicy.core.dto.PolicyCreationRequest;
import com.openpolicy.core.dto.PolicyResponse;
import com.openpolicy.core.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public List<PolicyResponse> getAllPolicies() {
        return policyService.getAllPolicies();
    }
    //Recibo un Request, devuelvo un Response
    @PostMapping
    public PolicyResponse createPolicy(@Valid @RequestBody PolicyCreationRequest request) {
        return policyService.createPolicy(request);
    }

    // GET /api/policies/{policyNumber}
    // Ejemplo: /api/policies/POL-1001
    @GetMapping("/{policyNumber}")
    public ResponseEntity<PolicyResponse> getPolicy(@PathVariable String policyNumber) {
        PolicyResponse policy = policyService.getPolicyByNumber(policyNumber);

        if (policy != null) {
            return ResponseEntity.ok(policy); // 200 OK con el JSON
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found si no existe
        }
    }
}