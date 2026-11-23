package com.volcan.pagos.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    private Double monto;
    private String metodoPago;
    private String estado;
    private String codigoTransaccion;
    private LocalDateTime fechaPago = LocalDateTime.now();
}