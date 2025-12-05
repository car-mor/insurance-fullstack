package com.openpolicy.core.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpolicy.core.model.Policy;
import com.openpolicy.core.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // ¡Esto levanta todo el contexto de Spring!
@AutoConfigureMockMvc // Configura el cliente HTTP simulado
class PolicyIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Nuestro "Navegador" simulado

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir Objetos a JSON

    @Test
    void shouldCreateAndRetrievePolicy() throws Exception {
        // 1. Crear una póliza nueva
        // Usamos "POL-99999" que sí cumple el formato POL-XXXX
        Policy newPolicy = new Policy(null, "POL-99999", "Integration User", new BigDecimal("5000"), LocalDate.now().plusDays(10), null);
        // 2. Hacer POST a la API (Simulando Postman/Swagger)
        mockMvc.perform(post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPolicy)))
                .andExpect(status().isOk()) // Esperamos HTTP 200
                .andExpect(jsonPath("$.id").exists()); // Esperamos que devuelva un ID

        // 3. Verificar que realmente se guardó en la DB (Haciendo GET)
        // Nota: Como schema.sql ya insertó 2, más la que acabamos de crear, esperamos 3 o más.
        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4))); // 2 (SQL) + 1 (Runner) + 1 (Test)
    }
}