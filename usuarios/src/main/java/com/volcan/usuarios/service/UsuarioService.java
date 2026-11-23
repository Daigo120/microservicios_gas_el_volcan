package com.volcan.usuarios.service;

import com.volcan.usuarios.model.Usuario;
import com.volcan.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() { return repository.findAll(); }
    
    public Usuario registrarCliente(Usuario usuario) {
        usuario.setRol("CLIENTE"); 
        
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    public Usuario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario guardar(Usuario usuario) {
       
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return repository.save(usuario); 
    }
    
    public void eliminar(Long id) { repository.deleteById(id); }

   
    public Usuario login(String correo, String passwordPlana) {
        return repository.findByCorreo(correo)
                .filter(u -> passwordEncoder.matches(passwordPlana, u.getPassword())) 
                .orElse(null);
    }
}