package com.volcan.pagos.repository;
import com.volcan.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {
}