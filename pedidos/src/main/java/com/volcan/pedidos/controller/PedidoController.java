package com.volcan.pedidos.controller;

import com.volcan.pedidos.dto.ErrorResponse;
import com.volcan.pedidos.model.Pedido;
import com.volcan.pedidos.service.PedidoService;
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

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    @Operation(summary = "Crear pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
            content = @Content(schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "400", description = "Datos del pedido inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error al reservar inventario o crear el pedido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Pedido> crear( @RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearPedido(pedido));
    }

    @PostMapping("/{id}/pagar")
    @Operation(summary = "Pagar pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido pagado correctamente",
            content = @Content(schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Pedido> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(service.pagarPedido(id));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado de pedidos obtenido correctamente")
    })
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado",
            content = @Content(schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}