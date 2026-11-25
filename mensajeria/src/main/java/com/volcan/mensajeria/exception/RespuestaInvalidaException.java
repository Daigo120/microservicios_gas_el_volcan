package com.volcan.mensajeria.exception;

public class RespuestaInvalidaException extends RuntimeException {
    public RespuestaInvalidaException() {
        super("La respuesta no puede estar vacía");
    }
}
