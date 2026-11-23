package com.volcan.despacho.service;

import com.volcan.despacho.model.Despacho;
import com.volcan.despacho.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class DespachoService {

    @Autowired
    private DespachoRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicios.pedidos.url}")
    private String urlPedidos;

    @Value("${servicios.usuarios.url}")
    private String urlUsuarios;

    
    public List<Despacho> listar() {
        return repository.findAll();
    }
    

    public Despacho generarDespacho(Long pedidoId) {
        Despacho despacho = new Despacho();
        despacho.setPedidoId(pedidoId);

        
        Map<String, Object> pedido = restTemplate.getForObject(urlPedidos + "/" + pedidoId, Map.class);
        if (pedido == null) throw new RuntimeException("Pedido no encontrado");

        despacho.setDireccion(pedido.get("direccion") != null ? pedido.get("direccion").toString() : "");
        despacho.setHorarioEntrega(pedido.get("horarioEntrega") != null ? pedido.get("horarioEntrega").toString() : "");
        despacho.setNotas(pedido.get("notas") != null ? pedido.get("notas").toString() : "");

        
        Long usuarioId = Long.valueOf(pedido.get("usuarioId").toString());
        Map<String, Object> usuario = restTemplate.getForObject(urlUsuarios + "/" + usuarioId, Map.class);
        
        if (usuario != null) {
            despacho.setNombreUsuario(usuario.get("nombre").toString());
            despacho.setCorreoUsuario(usuario.get("correo").toString());
        } else {
            despacho.setNombreUsuario("Usuario Desconocido");
            despacho.setCorreoUsuario("Sin correo");
        }

        
        return repository.save(despacho);
    }
}