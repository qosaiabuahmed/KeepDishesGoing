package be.kdg.prog6.order.adapter.in.dto;

import java.math.BigDecimal;
import java.util.List;

public record BasketDto(
        String basketId,
        String restaurantId,
        List<BasketItemDto> items,
        BigDecimal totalPrice,
        int totalItemCount
) {}