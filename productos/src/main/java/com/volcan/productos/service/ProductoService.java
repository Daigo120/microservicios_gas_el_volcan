package com.volcan.productos.service;

import com.volcan.productos.model.Producto;
import com.volcan.productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repository;

    public List<Producto> listar() { return repository.findAll(); }
    public Producto guardar(Producto p) { return repository.save(p); }
    
    // --- AGREGA ESTE MÉTODO ---
    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }
    // --------------------------

    public Producto actualizar(Long id, Producto p) {
        Producto existente = repository.findById(id).orElseThrow();
        existente.setNombre(p.getNombre());
        existente.setPrecio(p.getPrecio());
        existente.setStock(p.getStock());
        return repository.save(existente);
    }
    
    public void eliminar(Long id) { repository.deleteById(id); }
}