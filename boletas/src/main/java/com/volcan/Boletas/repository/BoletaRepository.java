package com.volcan.Boletas.repository;
import com.volcan.Boletas.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
}