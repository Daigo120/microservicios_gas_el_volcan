package com.volcan.usuarios.service;

import com.volcan.usuarios.model.Usuario;
import com.volcan.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarTodos() { return repository.findAll(); }
    
    public Usuario registrarCliente(Usuario usuario) {
        usuario.setRol("CLIENTE"); 
        return repository.save(usuario);
    }
    public Usuario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario guardar(Usuario usuario) { return repository.save(usuario); }
    
    public void eliminar(Long id) { repository.deleteById(id); }

    // Método de login
    public Usuario login(String correo, String password) {
        return repository.findByCorreo(correo)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }
}