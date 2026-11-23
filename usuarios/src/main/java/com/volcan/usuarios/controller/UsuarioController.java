package com.volcan.usuarios.controller;

import com.volcan.usuarios.model.Usuario;
import com.volcan.usuarios.service.UsuarioService;
import com.volcan.usuarios.util.JwtUtil; 
import com.volcan.usuarios.dto.LoginRequest; 
import com.volcan.usuarios.dto.LoginResponse; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {
    
    @Autowired
    private UsuarioService service;

    @Autowired
    private JwtUtil jwtUtil; 

    @GetMapping
    public List<Usuario> listar() { return service.listarTodos(); }

    @PostMapping("/registro")
    public Usuario registro(@RequestBody Usuario usuario) {
        return service.registrarCliente(usuario);
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        
        Usuario usuarioLogueado = service.login(request.getCorreo(), request.getPassword());
        
        if (usuarioLogueado != null) {
           
            String token = jwtUtil.generarToken(
                usuarioLogueado.getId(), 
                usuarioLogueado.getCorreo(), 
                usuarioLogueado.getRol() 
            );
            
            
            return ResponseEntity.ok(new LoginResponse(token, usuarioLogueado));
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id); 
    }
    

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}