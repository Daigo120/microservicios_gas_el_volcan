package com.volcan.pedidos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference; // <-- IMPORTANTE
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long usuarioId; 
    private Double total;
    private String estado; 
    private String direccion;
    private String horarioEntrega;
    private String notas; 
    
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // --- MODIFICA ESTO ---
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Evita el bucle infinito en el JSON
    private List<DetallePedido> detalles;

    // --- AGREGA ESTE MÉTODO HELPER ---
    // Esto asegura que cada detalle sepa a qué pedido pertenece antes de guardar
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
        if (detalles != null) {
            for (DetallePedido detalle : detalles) {
                detalle.setPedido(this);
            }
        }
    }
}