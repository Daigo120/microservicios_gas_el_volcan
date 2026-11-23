CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    run VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(50) NOT NULL, -- Residencial o Comercial
    rol VARCHAR(20) NOT NULL DEFAULT 'CLIENTE', -- Para el admin
    direccion VARCHAR(255) NOT NULL
);