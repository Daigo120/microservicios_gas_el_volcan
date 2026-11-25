package com.volcan.Boletas.service;

import com.volcan.Boletas.exception.PedidoNotFoundException;
import com.volcan.Boletas.model.Boleta;
import com.volcan.Boletas.repository.BoletaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicios.pedidos.url}")
    private String urlPedidos;

    public Boleta obtenerPorPedidoId(Long pedidoId) {
        return repository.findFirstByPedidoId(pedidoId).orElse(null);
    }

    public Boleta generarBoleta(Boleta boleta) throws Exception {
        
        Boleta boletaExistente = obtenerPorPedidoId(boleta.getPedidoId());
        if (boletaExistente != null) {
            return boletaExistente;
        }

        
        Map<String, Object> pedido = obtenerPedido(boleta.getPedidoId());

        boleta.setMontoTotal(Double.parseDouble(pedido.get("total").toString()));
        boleta.setUsuarioId(Long.valueOf(pedido.get("usuarioId").toString()));

        Object detalles = pedido.get("detalles");
        if (detalles != null) {
            ObjectMapper mapper = new ObjectMapper();
            boleta.setDetalleJson(mapper.writeValueAsString(detalles));
        } else {
            boleta.setDetalleJson("[]");
        }

        return repository.save(boleta);
    }

    private Map<String, Object> obtenerPedido(Long pedidoId) {
        String url = urlPedidos + "/" + pedidoId;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> pedido = restTemplate.getForObject(url, Map.class);
            if (pedido == null) {
                throw new PedidoNotFoundException(pedidoId);
            }
            return pedido;
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 404) {
                throw new PedidoNotFoundException(pedidoId);
            }
            throw new RuntimeException("Error al consultar pedido ID " + pedidoId
                    + " (HTTP " + e.getStatusCode().value() + "): " + e.getResponseBodyAsString());
        }
    }
}