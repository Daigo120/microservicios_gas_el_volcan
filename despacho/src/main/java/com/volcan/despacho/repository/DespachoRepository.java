package com.volcan.despacho.repository;
import com.volcan.despacho.model.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespachoRepository extends JpaRepository<Despacho, Long> {
}