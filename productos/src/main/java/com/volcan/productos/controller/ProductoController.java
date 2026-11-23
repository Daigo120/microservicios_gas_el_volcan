package com.volcan.productos.controller;
import com.volcan.productos.model.Producto;
import com.volcan.productos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    @Autowired
    private ProductoService service;

    @GetMapping
    public List<Producto> listar() { return service.listar(); }

    @PostMapping
    public Producto crear(@RequestBody Producto p) { return service.guardar(p); }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto p) {
        return service.actualizar(id, p);
    }
    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);  
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}