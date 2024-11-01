package br.com.ambev.orderquery.controller;

import br.com.ambev.orderquery.core.dto.OrderDTO;
import br.com.ambev.orderquery.core.service.ProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderQueryController.class)
public class OrderQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessingService processingService;

    private OrderDTO orderDTO;

    @BeforeEach
    public void setup() {
        orderDTO = mock(OrderDTO.class);
    }

    @Test
    public void testSearchOrderWhenValidOrderIdThenReturnOrderDTO() throws Exception {
        // Arrange
        String validOrderId = "12345";
        when(processingService.findOrder(anyString())).thenReturn(orderDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{order_id}", validOrderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idempotencyKey").value(orderDTO.idempotencyKey()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(orderDTO.status()));
    }
}
