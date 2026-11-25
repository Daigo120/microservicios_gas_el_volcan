package com.volcan.usuarios.service;

import com.volcan.usuarios.dto.UsuarioUpdateDTO;
import com.volcan.usuarios.exception.CredencialesInvalidasException;
import com.volcan.usuarios.exception.UsuarioNotFoundException;
import com.volcan.usuarios.model.Usuario;
import com.volcan.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired private UsuarioRepository repository;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() { return repository.findAll(); }

    public Usuario registrarCliente(Usuario usuario) {
        usuario.setRol("CLIENTE");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    public Usuario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    public Usuario guardar(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return repository.save(usuario);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public Usuario login(String correo, String passwordPlana) {
        return repository.findByCorreo(correo)
                .filter(u -> passwordEncoder.matches(passwordPlana, u.getPassword()))
                .orElseThrow(CredencialesInvalidasException::new);
    }

    public Usuario actualizar(Long id, UsuarioUpdateDTO datos) {
        Usuario existente = repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        existente.setRun(datos.getRun());
        existente.setNombre(datos.getNombre());
        existente.setApellidos(datos.getApellidos());
        existente.setCorreo(datos.getCorreo());
        existente.setTipoUsuario(datos.getTipoUsuario());
        if (datos.getDireccion() != null && !datos.getDireccion().isBlank()) {
            existente.setDireccion(datos.getDireccion());
        }
        return repository.save(existente);
    }
}