CREATE TABLE IF NOT EXISTS policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_number VARCHAR(50) NOT NULL,
    holder_name VARCHAR(100) NOT NULL,
    premium_amount DECIMAL(10, 2) NOT NULL,
    start_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE'
    );

-- Insertamos un par de datos de prueba
INSERT INTO policies (policy_number, holder_name, premium_amount, start_date, status)
VALUES ('POL-1001', 'Carlos Insurance', 1500.00, '2025-01-01', 'ACTIVE');

INSERT INTO policies (policy_number, holder_name, premium_amount, start_date, status)
VALUES ('POL-1002', 'Empresa Demo SA', 5000.50, '2025-02-15', 'PENDING');