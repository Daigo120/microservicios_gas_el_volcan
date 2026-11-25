package com.volcan.despacho.service;

import com.volcan.despacho.exception.PedidoNotFoundException;
import com.volcan.despacho.model.Despacho;
import com.volcan.despacho.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
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

        
        Map<String, Object> pedido = obtenerPedido(pedidoId);
        despacho.setDireccion(pedido.get("direccion") != null ? pedido.get("direccion").toString() : "");
        despacho.setHorarioEntrega(pedido.get("horarioEntrega") != null ? pedido.get("horarioEntrega").toString() : "");
        despacho.setNotas(pedido.get("notas") != null ? pedido.get("notas").toString() : "");

        
        Long usuarioId = Long.valueOf(pedido.get("usuarioId").toString());
        Map<String, Object> usuario = obtenerUsuarioOpcional(usuarioId);
        if (usuario != null) {
            despacho.setNombreUsuario(usuario.get("nombre") != null ? usuario.get("nombre").toString() : "Usuario Desconocido");
            despacho.setCorreoUsuario(usuario.get("correo") != null ? usuario.get("correo").toString() : "Sin correo");
        } else {
            despacho.setNombreUsuario("Usuario Desconocido");
            despacho.setCorreoUsuario("Sin correo");
        }

        return repository.save(despacho);
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

    private Map<String, Object> obtenerUsuarioOpcional(Long usuarioId) {
        String url = urlUsuarios + "/" + usuarioId;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> usuario = restTemplate.getForObject(url, Map.class);
            return usuario;
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 404) {
                return null;   // usuario no existe → "Usuario Desconocido"
            }
            throw new RuntimeException("Error al consultar usuario ID " + usuarioId
                    + " (HTTP " + e.getStatusCode().value() + "): " + e.getResponseBodyAsString());
        }
    }
}