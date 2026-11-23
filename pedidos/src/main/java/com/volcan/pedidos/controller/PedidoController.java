package com.volcan.pedidos.controller;

import com.volcan.pedidos.model.Pedido;
import com.volcan.pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    
    @Autowired
    private PedidoService service;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pedido pedido) { 
        try {
            Pedido nuevoPedido = service.crearPedido(pedido);
            return ResponseEntity.ok(nuevoPedido);
        } catch (Exception e) {
            
            e.printStackTrace(); 
            return ResponseEntity.internalServerError().body("ERROR REAL: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/pagar")
    public Pedido pagar(@PathVariable Long id) { 
        return service.pagarPedido(id); 
    }

    @GetMapping
    public List<Pedido> listar() { 
        return service.listar(); 
    }
    @GetMapping("/{id}")
    public Pedido obtenerPorId(@PathVariable Long id) {
    
    return service.obtenerPorId(id); 
}
}