package be.kdg.prog6.order.adapter.in.request;

import java.math.BigDecimal;

public record AddDishToBasketRequest(
        String restaurantId,
        String dishId,
        String dishName,
        BigDecimal unitPrice,
        int quantity
) {}