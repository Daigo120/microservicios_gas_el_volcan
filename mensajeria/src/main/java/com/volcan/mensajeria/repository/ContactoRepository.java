package com.volcan.mensajeria.repository;

import com.volcan.mensajeria.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
}