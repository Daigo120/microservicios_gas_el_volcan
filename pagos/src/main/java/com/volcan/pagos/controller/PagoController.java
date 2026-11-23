package com.volcan.pagos.controller;

import com.volcan.pagos.model.Pago;
import com.volcan.pagos.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {
    
    @Autowired
    private PagoService service;

    @PostMapping
    public Pago procesar(@RequestBody Pago pago) {
        return service.procesarPago(pago);
    }
}