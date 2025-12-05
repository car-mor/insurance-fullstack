package com.openpolicy.core.repository;

import com.openpolicy.core.model.Policy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Date;


import java.util.List;

@Repository
public class PolicyRepository {

    private final JdbcTemplate jdbcTemplate;

    // Inyección de dependencias por constructor
    public PolicyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // El RowMapper es el "traductor": Convierte una fila de SQL (ResultSet) a un Objeto Java (Policy)
    private final RowMapper<Policy> policyRowMapper = (rs, rowNum) -> {
        Policy policy = new Policy();
        policy.setId(rs.getLong("id"));
        policy.setPolicyNumber(rs.getString("policy_number"));
        policy.setHolderName(rs.getString("holder_name"));
        policy.setPremiumAmount(rs.getBigDecimal("premium_amount"));
        policy.setStartDate(rs.getDate("start_date").toLocalDate());
        policy.setStatus(rs.getString("status"));
        return policy;
    };

    // Método para obtener todas las pólizas
    public List<Policy> findAll() {
        String sql = "SELECT * FROM policies";
        return jdbcTemplate.query(sql, policyRowMapper);
    }

    // Método para guardar una nueva póliza y retornar el objeto con su nuevo ID
    // Solo acepta objetos 'Policy'. Si intentas meter un 'PolicyDto', explota (no compila).
    public Policy save(Policy policy) {
        String sql = "INSERT INTO policies (policy_number, holder_name, premium_amount, start_date, status) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            // Indicamos que queremos recuperar las llaves generadas (el ID)
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, policy.getPolicyNumber());
            ps.setString(2, policy.getHolderName());
            ps.setBigDecimal(3, policy.getPremiumAmount());
            // Convertimos LocalDate (Java) a Date (SQL)
            ps.setDate(4, Date.valueOf(policy.getStartDate()));
            ps.setString(5, policy.getStatus());
            return ps;
        }, keyHolder);

        // Recuperamos el ID generado y lo asignamos al objeto policy
        Number key = keyHolder.getKey();
        if (key != null) {
            policy.setId(key.longValue());
        }

        return policy;
    }
}