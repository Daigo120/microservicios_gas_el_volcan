package com.volcan.Boletas.controller;

import com.volcan.Boletas.dto.ErrorResponse;
import com.volcan.Boletas.exception.BoletaNotFoundException;
import com.volcan.Boletas.model.Boleta;
import com.volcan.Boletas.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin(origins = "*")
public class BoletaController {

    @Autowired
    private BoletaService service;

    @PostMapping
    @Operation(summary = "Generar boleta")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Boleta generada exitosamente",
            content = @Content(schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno al generar la boleta",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Boleta> generar(@RequestBody Boleta boleta) throws Exception {
        Boleta creada = service.generarBoleta(boleta);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener boleta por ID de pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Boleta encontrada",
            content = @Content(schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "Boleta no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Boleta> obtenerPorPedidoId(@PathVariable Long pedidoId) {
        Boleta boleta = service.obtenerPorPedidoId(pedidoId);
        if (boleta == null) {
            throw new BoletaNotFoundException(pedidoId);
        }
        return ResponseEntity.ok(boleta);
    }
}