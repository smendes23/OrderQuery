package br.com.ambev.orderquery.core.dto;

import lombok.Builder;

@Builder
public record OrderIdentificationDTO(String idempotencyKey) {
}
