package be.kdg.prog6.order.adapter.in.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        String dishId,
        String dishName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal totalPrice
) {}