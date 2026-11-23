package com.volcan.mensajeria.controller;
import com.volcan.mensajeria.model.Contacto;
import com.volcan.mensajeria.service.MensajeriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin(origins = "*")
public class MensajeriaController {
    @Autowired
    private MensajeriaService service;

    @PostMapping
    public Contacto enviar(@RequestBody Contacto c) { return service.guardarMensaje(c); }

    @GetMapping
    public List<Contacto> listar() { return service.listarMensajes(); }

    @PostMapping("/{id}/responder")
    public Contacto responder(@PathVariable Long id, @RequestBody String respuesta) {
        return service.responder(id, respuesta);
    }
}

