package com.volcan.pagos.controller;

import com.volcan.pagos.model.Pago;
import com.volcan.pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private PagoService service;

    @PostMapping
    @Operation(summary = "Procesar pago")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Pago procesado exitosamente",
            content = @Content(schema = @Schema(implementation = Pago.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de pago inválidos")
    })
    public ResponseEntity<Pago> procesar(@Valid @RequestBody Pago pago) {
        Pago procesado = service.procesarPago(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(procesado);
    }
}