package com.volcan.despacho.controller;

import com.volcan.despacho.dto.ErrorResponse;
import com.volcan.despacho.exception.PedidoIdRequeridoException;
import com.volcan.despacho.model.Despacho;
import com.volcan.despacho.service.DespachoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/despacho")
@CrossOrigin(origins = "*")
public class DespachoController {

    @Autowired
    private DespachoService service;

    @PostMapping
    @Operation(summary = "Crear despacho")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Despacho creado exitosamente",
            content = @Content(schema = @Schema(implementation = Despacho.class))),
        @ApiResponse(responseCode = "400", description = "Falta pedidoId",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Despacho> crear(@RequestBody Map<String, Long> body) {
        Long pedidoId = body.get("pedidoId");
        if (pedidoId == null) {
            throw new PedidoIdRequeridoException();
        }
        Despacho despachoCreado = service.generarDespacho(pedidoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(despachoCreado);
    }

    @GetMapping
    @Operation(summary = "Listar despachos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado de despachos obtenido correctamente")
    })
    public ResponseEntity<List<Despacho>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}