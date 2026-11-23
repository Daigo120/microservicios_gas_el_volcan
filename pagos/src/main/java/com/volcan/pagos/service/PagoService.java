package com.volcan.pagos.service;

import com.volcan.pagos.model.Pago;
import com.volcan.pagos.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicios.pedidos.url}")
    private String urlPedidos;

    public Pago procesarPago(Pago pago) {
        
        if (pago.getMonto() != null && pago.getMonto() > 0) {
            pago.setEstado("APROBADO");
            pago.setCodigoTransaccion(UUID.randomUUID().toString());
            
            
            Pago pagoGuardado = repository.save(pago);

           
            String url = urlPedidos + "/" + pago.getPedidoId() + "/pagar";
            try {
                restTemplate.postForObject(url, null, String.class);
            } catch (Exception e) {
                System.err.println("Error al notificar a pedidos-service: " + e.getMessage());
            }

            return pagoGuardado;
        } else {
            pago.setEstado("RECHAZADO");
            return repository.save(pago);
        }
    }
}