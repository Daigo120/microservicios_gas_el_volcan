package com.volcan.productos.exception;

public class CantidadInvalidaException extends RuntimeException {
    public CantidadInvalidaException(Integer cantidad) {
        super("Cantidad inválida: " + cantidad + ". Debe ser mayor a 0");
    }
}