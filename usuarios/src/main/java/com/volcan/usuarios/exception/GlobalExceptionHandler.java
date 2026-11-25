package com.volcan.usuarios.exception;

import com.volcan.usuarios.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UsuarioNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCredenciales(CredencialesInvalidasException e) {
        return build(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");
        return build(HttpStatus.BAD_REQUEST, new RuntimeException(msg));
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