package com.volcan.despacho.controller;

import com.volcan.despacho.model.Despacho;
import com.volcan.despacho.service.DespachoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/despacho")
@CrossOrigin(origins = "*")
public class DespachoController {

    @Autowired
    private DespachoService service;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Long> body) {
        try {
            Long pedidoId = body.get("pedidoId");
            if (pedidoId == null) return ResponseEntity.badRequest().body("Falta pedidoId");
            
            return ResponseEntity.ok(service.generarDespacho(pedidoId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("ERROR: " + e.getMessage());
        }
    }
}