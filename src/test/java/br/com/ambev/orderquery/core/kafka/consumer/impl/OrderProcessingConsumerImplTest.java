package br.com.ambev.orderquery.core.kafka.consumer.impl;

import br.com.ambev.orderquery.core.dto.OrderIdentificationDTO;
import br.com.ambev.orderquery.core.exception.ConsumerException;
import br.com.ambev.orderquery.core.service.ProcessingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderProcessingConsumerImplTest {

    @Mock
    private ProcessingService service;

    @InjectMocks
    private OrderProcessingConsumerImpl orderProcessingConsumer;

    @Test
    public void testProcessOrderWhenValidOrderThenValidateOrderUpdateCalled() {
        // Arrange
        OrderIdentificationDTO order = mock(OrderIdentificationDTO.class);

        // Act
        orderProcessingConsumer.processOrder(order);

        // Assert
        verify(service, times(1)).validateOrderUpdate(order);
    }

    @Test
    public void testProcessOrderWhenValidateOrderUpdateThrowsExceptionThenConsumerExceptionThrown() {
        // Arrange
        OrderIdentificationDTO order = mock(OrderIdentificationDTO.class);
        doThrow(new RuntimeException("Validation error")).when(service).validateOrderUpdate(order);

        // Act & Assert
        assertThrows(ConsumerException.class, () -> orderProcessingConsumer.processOrder(order));
    }
}
