package com.volcan.pedidos.model;

import jakarta.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data 
@Entity
@Table(name = "detalle_pedidos")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long productoId; 
    private Integer cantidad;
    private Double precioUnitario;
    private String nombreProducto;

  
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference 
    private Pedido pedido;
}