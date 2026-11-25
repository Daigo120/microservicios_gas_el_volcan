package com.volcan.Boletas.exception;

public class BoletaNotFoundException extends RuntimeException {
    public BoletaNotFoundException(Long pedidoId) {
        super("Boleta no encontrada para el pedido ID: " + pedidoId);
    }
}