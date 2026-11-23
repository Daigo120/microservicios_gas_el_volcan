CREATE TABLE despacho (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    nombre_usuario VARCHAR(100) NOT NULL,
    correo_usuario VARCHAR(100) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    horario_entrega VARCHAR(100),
    notas VARCHAR(255),
    estado VARCHAR(50) DEFAULT 'EN CAMINO', 
    fecha_despacho DATETIME DEFAULT CURRENT_TIMESTAMP
);