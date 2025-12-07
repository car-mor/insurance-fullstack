package com.openpolicy.core;

//import com.openpolicy.core.model.Policy;
//import com.openpolicy.core.dto.PolicyDto; porque ya tiene definido el request y response
//import com.openpolicy.core.service.PolicyServiceDto;
import com.openpolicy.core.dto.PolicyCreationRequest;
import com.openpolicy.core.dto.PolicyResponse;
import com.openpolicy.core.service.PolicyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.openpolicy.core.repository.PolicyRepository;
import com.openpolicy.core.model.Policy;
import com.openpolicy.core.model.Claim;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class CoreSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreSystemApplication.class, args);
	}

	// Se ejecuta justo después de que la aplicación arranca
//	@Bean
//	CommandLineRunner runner(PolicyRepository repository) {
//		return args -> {
//			System.out.println("--- CREANDO NUEVA PÓLIZA ---");
//			// 1. Crear el objeto (ID es null porque es nuevo)
//			Policy newPolicy = new Policy(null, "POL-NEW-999", "Juan Perez", new BigDecimal("2500.50"), LocalDate.now(), "ACTIVE");
//
//			// 2. Guardar en BD
//			Policy savedPolicy = repository.save(newPolicy);
//			System.out.println("¡Éxito! Póliza guardada con ID: " + savedPolicy.getId());
//
//			System.out.println("\n--- LISTA ACTUALIZADA ---");
//			repository.findAll().forEach(p -> System.out.println(p));
//		};
//	}

//	@Bean
//	CommandLineRunner runner(PolicyService policyService) {
//		return args -> {
//			//System.out.println("--- CREANDO NUEVA PÓLIZA DE PRUEBA (CON DTO) ---");
//
//			//Así se hacía reutilizando PolicyDto
////			// Creamos un DTO en lugar de una Entidad
////			PolicyDto newPolicyDto = new PolicyDto();
////			newPolicyDto.setPolicyNumber("POL-NEW-999");
////			newPolicyDto.setHolderName("Maria Service");
////			newPolicyDto.setPremiumAmount(new BigDecimal("3000.00"));
////			newPolicyDto.setStartDate(LocalDate.now());
////			// No seteamos status, el servicio lo pondrá en PENDING automáticamente
////
////			// Ahora sí, pasamos el DTO al servicio
////			PolicyDto savedPolicy = policyService.createPolicy(newPolicyDto);
////
////			System.out.println("¡Éxito! Póliza creada. ID: " + savedPolicy.getId());
//
////			System.out.println("\n--- LISTA ACTUALIZADA ---");
////			policyService.getAllPolicies().forEach(System.out::println);
//			System.out.println("--- CREANDO PÓLIZA DE PRUEBA PROFESIONAL ---");
//
//			PolicyCreationRequest request = new PolicyCreationRequest();
//			request.setPolicyNumber("POL-NEW-999");
//			request.setHolderName("Maria Service");
//			request.setPremiumAmount(new BigDecimal("3000.00"));
//			request.setStartDate(LocalDate.now());
//			// Nota: Aquí YA NO PUEDES setear ID ni Status. ¡Es seguro!
//
//			PolicyResponse response = policyService.createPolicy(request);
//
//			System.out.println("¡Éxito! ID: " + response.getId() + " Estado: " + response.getStatus());
//		};
//	}

	// Inyectamos el repositorio para esta prueba rápida
	// @Bean
	// CommandLineRunner runner(PolicyRepository repository) {
	// 	return args -> {
	// 		System.out.println("--- GENERANDO DATOS DE SINIESTROS ---");

	// 		// 1. Crear la Póliza
	// 		Policy p = new Policy();
	// 		p.setPolicyNumber("POL-CON-SINIESTROS");
	// 		p.setHolderName("Empresa de Transportes");
	// 		p.setPremiumAmount(new BigDecimal("10000"));
	// 		p.setStartDate(LocalDate.now());
	// 		p.setStatus("ACTIVE");

	// 		// 2. Crear Siniestros
	// 		Claim c1 = new Claim(null, "CLM-001", "Choque Leve", LocalDate.now(), new BigDecimal("500"), p);
	// 		Claim c2 = new Claim(null, "CLM-002", "Pérdida Total", LocalDate.now(), new BigDecimal("15000"), p);

	// 		// 3. Asignar los siniestros a la póliza
	// 		p.setClaims(List.of(c1, c2));

	// 		// 4. Guardar (Gracias a CascadeType.ALL, al guardar Póliza se guardan los Claims)
	// 		repository.save(p);

	// 		System.out.println("¡Póliza guardada con " + p.getClaims().size() + " siniestros!");
	// 	};
	// }
    @Bean
    CommandLineRunner runner(PolicyRepository repository) {
        return args -> {
            // ... (Tu código de limpiar y guardar sigue igual) ...
            repository.deleteAll();
            // ... (Creación de p1, p2, claims, saveAll...) ...
            Policy p1 = new Policy(null, "POL-A", "Cliente A", new BigDecimal("100"), LocalDate.now(), "ACTIVE", new java.util.ArrayList<>());
            // ... (asegúrate de volver a poner el código de creación de datos aquí)
            // ...
            // repository.saveAll(...)

            System.out.println("\n--- 2. LEYENDO DATOS (OPTIMIZADO CON JOIN FETCH) ---");

            // CAMBIO CLAVE: Usamos el método optimizado
            List<Policy> policies = repository.findAllWithClaims();

            for (Policy policy : policies) {
                // Ahora esto NO fallará porque los claims ya vinieron cargados en la primera consulta
                System.out.println("Póliza: " + policy.getPolicyNumber() + " tiene " + policy.getClaims().size() + " siniestros.");
            }
            System.out.println("--------------------------------------------------");
        };
    }

}