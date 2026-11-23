package com.volcan.pedidos.service;

import com.volcan.pedidos.model.Pedido;
import com.volcan.pedidos.model.DetallePedido;
import com.volcan.pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicios.productos.url}")
    private String urlProductos;

    public Pedido crearPedido(Pedido pedido) {
        double totalCalculado = 0.0;

        for (DetallePedido detalle : pedido.getDetalles()) {
            String url = urlProductos + "/" + detalle.getProductoId();
            
            try {
                Map<String, Object> producto = restTemplate.getForObject(url, Map.class);
                
                if (producto != null) {
                    int stockDisponible = Integer.parseInt(producto.get("stock").toString());
                    double precioReal = Double.parseDouble(producto.get("precio").toString());

                    if (stockDisponible < detalle.getCantidad()) {
                        throw new RuntimeException("Stock insuficiente para el producto ID: " + detalle.getProductoId());
                    }

                    // Seteamos el precio y el nombre real del producto
                    detalle.setPrecioUnitario(precioReal);
                    detalle.setNombreProducto(producto.get("nombre").toString()); 
                    
                    totalCalculado += (precioReal * detalle.getCantidad());
                } else {
                    throw new RuntimeException("Producto no encontrado con ID: " + detalle.getProductoId());
                }
            } catch (Exception e) {
                System.err.println("Error consultando productos en URL: " + url);
                e.printStackTrace(); 
                throw new RuntimeException("Error al comunicarse con productos-service: " + e.getMessage());
            }
        }

        pedido.setTotal(totalCalculado);
        pedido.setEstado("PENDIENTE"); 
        
        return repository.save(pedido);
    }
    
    public Pedido pagarPedido(Long id) {
        Pedido p = repository.findById(id).orElseThrow();
        p.setEstado("PAGADO");
        return repository.save(p);
    }

    public Pedido obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }
    
    public List<Pedido> listar() { return repository.findAll(); }
}