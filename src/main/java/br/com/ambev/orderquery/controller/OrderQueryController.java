package br.com.ambev.orderquery.controller;

import br.com.ambev.orderquery.core.dto.OrderDTO;
import br.com.ambev.orderquery.core.service.ProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.of;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderQueryController {

    private final ProcessingService service;


    @GetMapping("/{order_id}")
    public ResponseEntity<OrderDTO> searchOrder(@PathVariable("order_id") String orderId) {

        log.info("Search for  order with order_id: {}",orderId);

        return of(orderId)
                .map(orderRequest -> ResponseEntity.ok(service.findOrder(orderId)))
                .orElseThrow();
    }
}