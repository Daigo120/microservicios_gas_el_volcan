package com.volcan.pedidos.service;

import com.volcan.pedidos.exception.PedidoNotFoundException;
import com.volcan.pedidos.model.Pedido;
import com.volcan.pedidos.model.DetallePedido;
import com.volcan.pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PedidoService {

    @Autowired private PedidoRepository repository;
    @Autowired private RestTemplate restTemplate;

    @Value("${servicios.productos.url}")
    private String urlProductos;
    @Value("${seguridad.token-interno}")
    private String tokenInterno;

    public Pedido crearPedido(Pedido pedido) {
        double totalCalculado = 0.0;
        Map<Long, Integer> cantidadesPorProducto = new LinkedHashMap<>();

        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor que cero para el producto ID: "
                        + detalle.getProductoId());
            }
            cantidadesPorProducto.merge(detalle.getProductoId(), detalle.getCantidad(), Integer::sum);

            Map<String, Object> producto = obtenerProducto(detalle.getProductoId());
            double precioReal = Double.parseDouble(producto.get("precio").toString());
            detalle.setPrecioUnitario(precioReal);
            detalle.setNombreProducto(producto.get("nombre").toString());
            totalCalculado += precioReal * detalle.getCantidad();
        }

        Map<Long, Integer> descontados = new LinkedHashMap<>();
        try {
            for (Map.Entry<Long, Integer> entry : cantidadesPorProducto.entrySet()) {
                descontarStockRemoto(entry.getKey(), entry.getValue());
                descontados.put(entry.getKey(), entry.getValue());
            }
            pedido.setTotal(totalCalculado);
            pedido.setEstado("PENDIENTE");
            return repository.save(pedido);
        } catch (Exception e) {
            for (Map.Entry<Long, Integer> entry : descontados.entrySet()) {
                try {
                    restTemplate.postForEntity(
                            urlProductos + "/interno/" + entry.getKey() + "/reponer-stock?cantidad=" + entry.getValue(),
                            new HttpEntity<>(headersInternos()),
                            Void.class);
                } catch (Exception compensationError) {
                    System.err.println("No se pudo reponer stock del producto "
                            + entry.getKey() + ": " + compensationError.getMessage());
                }
            }
            throw new RuntimeException("No fue posible crear el pedido y reservar el inventario: "
                    + e.getMessage(), e);
        }
    }

    private Map<String, Object> obtenerProducto(Long productoId) {
        String url = urlProductos + "/" + productoId;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> producto = restTemplate.getForObject(url, Map.class);
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + productoId);
            }
            return producto;
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("Producto no encontrado con ID: " + productoId);
            }
            throw new RuntimeException("Error al consultar producto ID " + productoId
                    + " (HTTP " + e.getStatusCode().value() + "): " + e.getResponseBodyAsString());
        }
    }

    private void descontarStockRemoto(Long id, Integer cantidad) {
        String url = urlProductos + "/interno/" + id + "/descontar-stock?cantidad=" + cantidad;
        try {
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(headersInternos()), Void.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 400 -> throw new RuntimeException("Cantidad inválida para producto ID: " + id);
                case 403 -> throw new RuntimeException("Token interno inválido al descontar stock del producto ID: " + id);
                case 404 -> throw new RuntimeException("Producto no encontrado con ID: " + id);
                case 409 -> throw new RuntimeException("Stock insuficiente para producto ID: " + id);
                default  -> throw new RuntimeException("Error al descontar stock del producto ID " + id
                        + " (HTTP " + e.getStatusCode().value() + "): " + e.getResponseBodyAsString());
            }
        }
    }

    private HttpHeaders headersInternos() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", tokenInterno);
        return headers;
    }

    public Pedido pagarPedido(Long id) {
        Pedido p = repository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));
        p.setEstado("PAGADO");
        return repository.save(p);
    }

    public Pedido obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));
    }

    public List<Pedido> listar() { return repository.findAll(); }
}