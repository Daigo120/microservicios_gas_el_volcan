package com.volcan.productos.controller;

import com.volcan.productos.dto.ErrorResponse;
import com.volcan.productos.model.Producto;
import com.volcan.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @Value("${seguridad.token-interno}")
    private String tokenInterno;

    @GetMapping
    @Operation(summary = "Listar productos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado de productos obtenido correctamente")
    })
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping
    @Operation(summary = "Crear producto")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Datos del producto inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Producto> crear( @RequestBody Producto p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(p));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Datos del producto inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto p) {
        return ResponseEntity.ok(service.actualizar(id, p));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping("/interno/{id}/descontar-stock")
    @Operation(summary = "Descontar stock de un producto")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Stock descontado"),
        @ApiResponse(responseCode = "400", description = "Cantidad inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Token interno inválido"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Stock insuficiente",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> descontarStock(@PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestHeader("X-Internal-Token") String token) {
        if (!tokenInterno.equals(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        service.descontarStock(id, cantidad);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/interno/{id}/reponer-stock")
    @Operation(summary = "Reponer stock de un producto")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Stock repuesto"),
        @ApiResponse(responseCode = "400", description = "Cantidad inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Token interno inválido"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> reponerStock(@PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestHeader("X-Internal-Token") String token) {
        if (!tokenInterno.equals(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        service.reponerStock(id, cantidad);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}