package com.openpolicy.core.repository;

import com.openpolicy.core.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
// ¡MAGIA! Solo extendemos JpaRepository y Spring crea el código por nosotros.
// <Entidad, TipoDeLaID> -> <Policy, Long>

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    // No necesitamos escribir save(), findAll(), findById()... ¡Ya vienen hechos!
    // Spring lee "findByPolicyNumber" y automáticamente crea el SQL:
    // SELECT * FROM policies WHERE policy_number = ?
    Optional<Policy> findByPolicyNumber(String policyNumber);
    // "LEFT JOIN FETCH" le dice a Hibernate: "Trae la Póliza Y TAMBIÉN sus Siniestros de una vez"
    // "DISTINCT" es para evitar que si una póliza tiene 2 siniestros, aparezca 2 veces en la lista.
    @Query("SELECT DISTINCT p FROM Policy p LEFT JOIN FETCH p.claims")
    List<Policy> findAllWithClaims();
}