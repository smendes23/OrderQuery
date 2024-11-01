package br.com.ambev.orderquery.core.kafka.consumer;

import br.com.ambev.orderquery.core.dto.OrderIdentificationDTO;

public interface OrderProcessingConsumer {

    void processOrder(OrderIdentificationDTO order);
}
