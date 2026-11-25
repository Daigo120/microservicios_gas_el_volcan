package com.volcan.despacho.exception;

public class PedidoIdRequeridoException extends RuntimeException {
    public PedidoIdRequeridoException() {
        super("Falta pedidoId en el cuerpo de la petición");
    }
}