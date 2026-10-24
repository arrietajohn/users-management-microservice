-- =============================================
-- Script de creación de la base de datos
-- Gestión de Usuarios - Arquitectura Hexagonal
-- Variante PostgreSQL (ejecución manual)
--
-- A diferencia de schema.sql (MySQL), este script NO crea la base ni
-- selecciona el esquema: en PostgreSQL la base seprovisiona desde el
-- panel del proveedor (Supabase, Aiven, Render...) y el script se ejecuta
-- ya dentro de la base de datos destino, por ejemplo desde el SQL Editor
-- de Supabase.
--
-- Diferencias respecto al esquema de MySQL:
--   - ENUM  -> VARCHAR + CHECK (PostgreSQL no tiene ENUM nativo; el CHECK
--              conserva la misma restricción de dominio).
--   - ON UPDATE CURRENT_TIMESTAMP -> no existe; updated_at se actualiza
--              explícitamente en la sentencia UPDATE.
--   - TIMESTAMP en lugar de DATETIME.
-- =============================================

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN', 'MEMBER', 'REVIEWER')),
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED')),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Usuario administrador inicial (password: Admin1234!)
INSERT INTO users (id, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Administrador',
    'admin@example.com',
    '$2a$12$sfwkajsls.fktQb0Ngw5cOiSiV3z4LIwbucSAy4sw2wyNlxbUBt4q',
    'ADMIN',
    'ACTIVE'
)
ON CONFLICT (id) DO NOTHING;