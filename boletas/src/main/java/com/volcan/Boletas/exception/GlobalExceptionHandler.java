package com.volcan.Boletas.exception;

import com.volcan.Boletas.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePedidoNotFound(PedidoNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(BoletaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBoletaNotFound(BoletaNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                new RuntimeException("Error interno: " + e.getMessage()));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, Exception e) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), e.getMessage()));
    }
}