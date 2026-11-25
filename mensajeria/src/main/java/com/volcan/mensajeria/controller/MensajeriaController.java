package com.volcan.mensajeria.controller;

import com.volcan.mensajeria.dto.ErrorResponse;
import com.volcan.mensajeria.model.Contacto;
import com.volcan.mensajeria.service.MensajeriaService;
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
@RequestMapping("/api/mensajeria")
@CrossOrigin(origins = "*")
public class MensajeriaController {

    @Autowired
    private MensajeriaService service;

    @PostMapping
    @Operation(summary = "Enviar mensaje de contacto")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Mensaje enviado exitosamente",
            content = @Content(schema = @Schema(implementation = Contacto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de contacto inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Contacto> enviar(@Valid @RequestBody Contacto c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarMensaje(c));
    }

    @GetMapping
    @Operation(summary = "Listar mensajes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado de mensajes obtenido correctamente")
    })
    public ResponseEntity<List<Contacto>> listar() {
        return ResponseEntity.ok(service.listarMensajes());
    }

    @PostMapping("/{id}/responder")
    @Operation(summary = "Responder mensaje")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Respuesta registrada correctamente",
            content = @Content(schema = @Schema(implementation = Contacto.class))),
        @ApiResponse(responseCode = "400", description = "La respuesta no puede estar vacía",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Contacto> responder(@PathVariable Long id, @RequestBody String respuesta) {
        return ResponseEntity.ok(service.responder(id, respuesta));
    }
}