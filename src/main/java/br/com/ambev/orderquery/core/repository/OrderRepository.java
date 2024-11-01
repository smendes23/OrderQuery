package br.com.ambev.orderquery.core.repository;

import br.com.ambev.orderquery.core.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdempotencyKey(final  String idempotencyKey);
}
