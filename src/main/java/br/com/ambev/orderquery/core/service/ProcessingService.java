package br.com.ambev.orderquery.core.service;

import br.com.ambev.orderquery.core.dto.OrderDTO;
import br.com.ambev.orderquery.core.dto.OrderIdentificationDTO;

public interface ProcessingService {

    void validateOrderUpdate(final OrderIdentificationDTO orderDTO);
    OrderDTO findOrder(final String orderId);

}
