package br.com.ambev.orderquery.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_order")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Setter
    private String status;

    @Column(name = "idempotency_key")
    private String idempotencyKey;
}
