package com.volcan.productos.exception;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(Long id, Integer solicitado) {
        super("Stock insuficiente para producto " + id + ". Solicitado: " + solicitado);
    }
}
