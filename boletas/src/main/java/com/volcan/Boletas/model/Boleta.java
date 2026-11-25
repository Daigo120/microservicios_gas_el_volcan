package com.volcan.Boletas.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "boletas")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto total debe ser mayor que cero")
    private Double montoTotal;

    private LocalDateTime fechaEmision = LocalDateTime.now();

    @NotBlank(message = "El detalle de la boleta es obligatorio")
    @Column(columnDefinition = "TEXT")
    private String detalleJson;
}