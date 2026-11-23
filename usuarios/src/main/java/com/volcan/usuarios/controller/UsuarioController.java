package com.volcan.usuarios.controller;

import com.volcan.usuarios.model.Usuario;
import com.volcan.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar() { return service.listarTodos(); }

    @PostMapping("/registro")
    public Usuario registro(@RequestBody Usuario usuario) {
        return service.registrarCliente(usuario);
    }

    // ENDPOINT DE LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Usuario usuarioLogueado = service.login(usuario.getCorreo(), usuario.getPassword());
        
        if (usuarioLogueado != null) {
            // Si las credenciales son correctas, devolvemos el usuario (Status 200)
            return ResponseEntity.ok(usuarioLogueado);
        } else {
            // Si no, devolvemos un error 401 (No autorizado)
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }
    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
    return service.obtenerPorId(id); // Debe devolver un JSON con "nombre" y "correo"
}

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}