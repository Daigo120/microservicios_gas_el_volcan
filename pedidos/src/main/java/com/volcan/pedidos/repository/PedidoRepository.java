package com.volcan.pedidos.repository;
import com.volcan.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PedidoRepository extends JpaRepository<Pedido, Long> {}