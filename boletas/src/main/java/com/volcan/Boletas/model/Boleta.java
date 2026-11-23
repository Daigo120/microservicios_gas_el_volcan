package com.volcan.Boletas.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "boletas")
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    private Long usuarioId;
    private Double montoTotal;
    private LocalDateTime fechaEmision = LocalDateTime.now();
    
    @Column(columnDefinition = "TEXT")
    private String detalleJson; // Guarda los productos comprados en formato JSON
}