package com.volcan.despacho.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data 
@Entity
@Table(name = "despacho")
public class Despacho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    private String nombreUsuario;
    private String correoUsuario;
    private String direccion;
    private String horarioEntrega;
    private String notas;
    private String estado = "EN CAMINO";

    @Column(name = "fecha_despacho", insertable = false, updatable = false)
    private LocalDateTime fechaDespacho;
}