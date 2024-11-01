package br.com.ambev.orderquery.core.kafka.consumer.impl;

import br.com.ambev.orderquery.core.dto.OrderIdentificationDTO;
import br.com.ambev.orderquery.core.exception.ConsumerException;
import br.com.ambev.orderquery.core.kafka.consumer.OrderProcessingConsumer;
import br.com.ambev.orderquery.core.service.ProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProcessingConsumerImpl implements OrderProcessingConsumer {

    private final ProcessingService service;

    @KafkaListener(topics = "${kafka.topic.consumer}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void processOrder(final OrderIdentificationDTO order) {
        try{
            log.info("Received order: {}", order);
            service.validateOrderUpdate(order);
        }catch (Exception e){
            log.error("Error to process order: {}", order, e);
            throw new ConsumerException(e.getMessage());
        }
    }
}
