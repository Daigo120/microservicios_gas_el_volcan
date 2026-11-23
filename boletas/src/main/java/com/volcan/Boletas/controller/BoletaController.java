package com.volcan.Boletas.controller;

import com.volcan.Boletas.model.Boleta;
import com.volcan.Boletas.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin(origins = "*")
public class BoletaController {
    
    @Autowired
    private BoletaService service;

    @PostMapping
    public ResponseEntity<?> generar(@RequestBody Boleta boleta) throws Exception {
        return ResponseEntity.ok(service.generarBoleta(boleta));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerPorPedidoId(@PathVariable Long pedidoId) {
        Boleta boleta = service.obtenerPorPedidoId(pedidoId);
        if (boleta != null) {
            return ResponseEntity.ok(boleta);
        }
        return ResponseEntity.notFound().build();
    }
}