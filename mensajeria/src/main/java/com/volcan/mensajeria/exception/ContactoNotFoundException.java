package com.volcan.mensajeria.exception;

public class ContactoNotFoundException extends RuntimeException {
    public ContactoNotFoundException(Long id) {
        super("Mensaje de contacto no encontrado con ID: " + id);
    }
}
