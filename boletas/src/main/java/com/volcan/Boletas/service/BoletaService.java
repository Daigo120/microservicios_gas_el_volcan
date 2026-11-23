package com.volcan.Boletas.service;

import com.volcan.Boletas.model.Boleta;
import com.volcan.Boletas.repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper; // IMPORTANTE
import java.util.Map;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicios.pedidos.url}")
    private String urlPedidos;

    public Boleta generarBoleta(Boleta boleta) {
        String url = urlPedidos + "/" + boleta.getPedidoId();
        
        try {
            Map<String, Object> pedido = restTemplate.getForObject(url, Map.class);
            
            if (pedido != null) {
                boleta.setMontoTotal(Double.parseDouble(pedido.get("total").toString()));
                boleta.setUsuarioId(Long.valueOf(pedido.get("usuarioId").toString()));
                
                Object detalles = pedido.get("detalles");
                if (detalles != null) {
                    // Convertimos el objeto Java a un String JSON válido (con comillas dobles)
                    ObjectMapper mapper = new ObjectMapper();
                    boleta.setDetalleJson(mapper.writeValueAsString(detalles)); 
                } else {
                    boleta.setDetalleJson("[]"); // Si no hay detalles, guardamos un array vacío
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener datos del pedido: " + e.getMessage());
        }

        return repository.save(boleta);
    }
}