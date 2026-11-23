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
    public ResponseEntity<?> generar(@RequestBody Boleta boleta) {
        try {
            return ResponseEntity.ok(service.generarBoleta(boleta));
        } catch (Exception e) {
            e.printStackTrace(); // Para verlo en la terminal de VS Code
            return ResponseEntity.internalServerError().body("ERROR REAL EN BOLETAS: " + e.getMessage());
        }
    }
}