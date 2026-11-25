package com.volcan.productos.service;

import com.volcan.productos.exception.CantidadInvalidaException;
import com.volcan.productos.exception.ProductoNotFoundException;
import com.volcan.productos.exception.StockInsuficienteException;
import com.volcan.productos.model.Producto;
import com.volcan.productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto guardar(Producto p) {
        return repository.save(p);
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
    }

    public Producto actualizar(Long id, Producto p) {
        Producto existente = repository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        existente.setNombre(p.getNombre());
        existente.setDescripcion(p.getDescripcion());
        existente.setPrecio(p.getPrecio());
        existente.setStock(p.getStock());
        existente.setImagenUrl(p.getImagenUrl());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        
        
        if (!repository.existsById(id)) {
            throw new ProductoNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public void descontarStock(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new CantidadInvalidaException(cantidad);
        }
        if (!repository.existsById(id)) {
            throw new ProductoNotFoundException(id);
        }
        int filas = repository.descontarStock(id, cantidad);
        if (filas == 0) {
            throw new StockInsuficienteException(id, cantidad);
        }
    }

    public void reponerStock(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new CantidadInvalidaException(cantidad);
        }
        if (!repository.existsById(id)) {
            throw new ProductoNotFoundException(id);
        }
        int filas = repository.reponerStock(id, cantidad);
        if (filas == 0) {
            throw new RuntimeException("No se pudo reponer el stock del producto " + id);
        }
    }
}