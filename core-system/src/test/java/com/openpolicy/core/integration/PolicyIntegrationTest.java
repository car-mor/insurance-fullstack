package com.openpolicy.core.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpolicy.core.dto.AuthResponse;
import com.openpolicy.core.dto.LoginRequest;
import com.openpolicy.core.dto.PolicyCreationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PolicyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreatePolicy_WhenAuthorized() throws Exception {
        // 1. OBTENER TOKEN (Simular Login)
        LoginRequest loginRequest = new LoginRequest("admin", "admin123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Extraer el token del JSON de respuesta
        String responseString = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(responseString, AuthResponse.class);
        String token = authResponse.getToken();

        // 2. CREAR PÓLIZA (Usando el Token)
        PolicyCreationRequest newPolicy = new PolicyCreationRequest();
        newPolicy.setPolicyNumber("POL-8888");
        newPolicy.setHolderName("Integration Tester");
        newPolicy.setPremiumAmount(new BigDecimal("5000"));
        newPolicy.setStartDate(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/policies")
                        .header("Authorization", "Bearer " + token) // ¡Aquí va la magia!
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPolicy)))
                .andExpect(status().isOk()) // Esperamos 200 OK
                .andExpect(jsonPath("$.policyNumber").value("POL-8888"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldRejectPolicy_WhenNotAuthorized() throws Exception {
        // Intentar crear SIN token
        PolicyCreationRequest newPolicy = new PolicyCreationRequest();
        newPolicy.setPolicyNumber("POL-HACK");
        // ... (llenar datos) ...

        mockMvc.perform(post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPolicy)))
                .andExpect(status().isForbidden()); // Esperamos 403 Forbidden
    }
}