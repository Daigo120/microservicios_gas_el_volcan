package com.volcan.usuarios.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String run;

    private String nombre;
    private String apellidos;

    @Column(unique = true)
    private String correo;

    private String password;
    private String tipoUsuario;
    private String direccion;
    private String rol; 
}