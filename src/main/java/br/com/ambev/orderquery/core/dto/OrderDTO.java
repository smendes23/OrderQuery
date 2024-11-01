package br.com.ambev.orderquery.core.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderDTO(
        List<ProductDTO> products,
        BigDecimal totalPrice,
        String status,
        String idempotencyKey
) {
    public OrderDTO withStatus(String status){
        return new OrderDTO(products(), BigDecimal.ZERO, status, idempotencyKey());
    }

    public OrderDTO withTotalAmount(BigDecimal totalAmount){
        return new OrderDTO(products(), totalAmount, status(), idempotencyKey());
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "products=" + products +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                '}';
    }
}
