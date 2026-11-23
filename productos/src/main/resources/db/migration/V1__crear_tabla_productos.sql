CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    imagen_url VARCHAR(255)
);