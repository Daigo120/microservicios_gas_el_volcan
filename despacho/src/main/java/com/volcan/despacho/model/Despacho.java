package com.volcan.despacho.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "despacho")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "El correo del usuario es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String correoUsuario;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    private String direccion;

    @NotBlank(message = "El horario de entrega es obligatorio")
    private String horarioEntrega;

    private String notas;

    @NotBlank(message = "El estado del despacho es obligatorio")
    private String estado = "EN CAMINO";

    @Column(name = "fecha_despacho", insertable = false, updatable = false)
    private LocalDateTime fechaDespacho;
}