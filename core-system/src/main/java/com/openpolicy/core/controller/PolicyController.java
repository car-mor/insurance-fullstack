package com.openpolicy.core.controller;

import com.openpolicy.core.dto.PolicyCreationRequest;
import com.openpolicy.core.dto.PolicyResponse;
import com.openpolicy.core.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
}