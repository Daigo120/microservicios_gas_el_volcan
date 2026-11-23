CREATE TABLE boletas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    monto_total DOUBLE NOT NULL,
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    detalle_json TEXT -- Aquí guardamos un JSON con los productos comprados
);