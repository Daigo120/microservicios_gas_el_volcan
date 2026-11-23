package com.volcan.mensajeria.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contactos")
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String correo;
    private String mensaje;
    private LocalDateTime fecha = LocalDateTime.now();
    private Boolean respondido = false;
    private String respuesta;
}