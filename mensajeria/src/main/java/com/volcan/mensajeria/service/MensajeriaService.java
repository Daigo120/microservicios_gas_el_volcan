package com.volcan.mensajeria.service;
import com.volcan.mensajeria.model.Contacto;
import com.volcan.mensajeria.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MensajeriaService {
    @Autowired
    private ContactoRepository repository;

    public Contacto guardarMensaje(Contacto c) { return repository.save(c); }
    public List<Contacto> listarMensajes() { return repository.findAll(); }
    
    public Contacto responder(Long id, String respuesta) {
        Contacto c = repository.findById(id).orElseThrow();
        c.setRespuesta(respuesta);
        c.setRespondido(true);
        return repository.save(c);
    }
}