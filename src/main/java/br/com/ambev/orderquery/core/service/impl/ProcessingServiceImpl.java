package br.com.ambev.orderquery.core.service.impl;

import br.com.ambev.orderquery.core.dto.OrderDTO;
import br.com.ambev.orderquery.core.dto.OrderIdentificationDTO;
import br.com.ambev.orderquery.core.dto.ProductDTO;
import br.com.ambev.orderquery.core.repository.OrderRepository;
import br.com.ambev.orderquery.core.repository.ProductRepository;
import br.com.ambev.orderquery.core.service.ProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessingServiceImpl implements ProcessingService {

    private final OrderRepository repository;
    private final ProductRepository productRepository;

    @Override
    public void validateOrderUpdate(OrderIdentificationDTO orderDTO) {
        log.info("Search and sum total price to product with idempotencyKey: {}", orderDTO.idempotencyKey());

        BigDecimal totalPrice = productRepository.findByIdempotencyKey(orderDTO.idempotencyKey())
                .stream()
                .map(prod-> prod.getPrice().multiply(BigDecimal.valueOf(prod.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow();


        log.info("Validating order update: {}", orderDTO);

        repository.findByIdempotencyKey(orderDTO.idempotencyKey())
                .map(orderQuery -> {
                    orderQuery.setTotalPrice(totalPrice);
                    orderQuery.setStatus("APROVADO");
                    return repository.save(orderQuery);
                })
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Cacheable(value = "orderDTO", key = "#orderId")
    public OrderDTO findOrder(final String orderId) {
        return repository.findByIdempotencyKey(orderId)
                .map(order -> OrderDTO.builder()
                        .idempotencyKey(order.getIdempotencyKey())
                        .status(order.getStatus())
                        .totalPrice(order.getTotalPrice())
                        .products(findProductById(order.getIdempotencyKey()))
                        .build())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    private List<ProductDTO> findProductById(final String idempotencyKey) {
        return productRepository. findByIdempotencyKey(idempotencyKey)
                .stream()
                .map(product -> ProductDTO.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .build())
                .toList();
    }
}
