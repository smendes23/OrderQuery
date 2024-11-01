package br.com.ambev.orderquery.core.service.impl;

import br.com.ambev.orderquery.core.dto.OrderDTO;
import br.com.ambev.orderquery.core.dto.OrderIdentificationDTO;
import br.com.ambev.orderquery.core.dto.ProductDTO;
import br.com.ambev.orderquery.core.entity.Order;
import br.com.ambev.orderquery.core.entity.Product;
import br.com.ambev.orderquery.core.repository.OrderRepository;
import br.com.ambev.orderquery.core.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessingServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProcessingServiceImpl processingService;

    @Test
    public void testValidateOrderUpdateWhenOrderIsUpdatedThenStatusIsApproved() {
        // Arrange
        String idempotencyKey = "test-key";
        OrderIdentificationDTO orderDTO = new OrderIdentificationDTO(idempotencyKey);
        List<Product> products = List.of(
                new Product(1L, "Product1", 2, BigDecimal.valueOf(10), idempotencyKey),
                new Product(2L, "Product2", 1, BigDecimal.valueOf(20), idempotencyKey)
        );
        Order order = new Order(1L, BigDecimal.ZERO, "PENDING", idempotencyKey);

        when(productRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(products);
        when(orderRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        processingService.validateOrderUpdate(orderDTO);

        // Assert
        verify(orderRepository).save(argThat(savedOrder -> 
            savedOrder.getStatus().equals("APROVADO") &&
            savedOrder.getTotalPrice().equals(BigDecimal.valueOf(40))
        ));
    }

    @Test
    public void testValidateOrderUpdateWhenOrderNotFoundThenThrowException() {
        // Arrange
        String idempotencyKey = "test-key";
        OrderIdentificationDTO orderDTO = new OrderIdentificationDTO(idempotencyKey);
        List<Product> products = List.of(
                new Product(1L, "Product1", 2, BigDecimal.valueOf(10), idempotencyKey),
                new Product(2L, "Product2", 1, BigDecimal.valueOf(20), idempotencyKey)
        );

        when(productRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(products);
        when(orderRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> processingService.validateOrderUpdate(orderDTO));
    }

    @Test
    public void testFindOrderWhenOrderIsFoundThenReturnOrderDTO() {
        // Arrange
        String idempotencyKey = "test-key";
        Order order = new Order(1L, BigDecimal.valueOf(40), "APROVADO", idempotencyKey);
        List<Product> products = List.of(
                new Product(1L, "Product1", 2, BigDecimal.valueOf(10), idempotencyKey),
                new Product(2L, "Product2", 1, BigDecimal.valueOf(20), idempotencyKey)
        );

        when(orderRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(order));
        when(productRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(products);

        // Act
        OrderDTO result = processingService.findOrder(idempotencyKey);

        // Assert
        assertEquals(idempotencyKey, result.idempotencyKey());
        assertEquals("APROVADO", result.status());
        assertEquals(BigDecimal.valueOf(40), result.totalPrice());
    }

    @Test
    public void testFindOrderWhenOrderNotFoundThenThrowException() {
        String idempotencyKey = "non-existing-key";

        when(orderRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> processingService.findOrder(idempotencyKey));
    }
}
