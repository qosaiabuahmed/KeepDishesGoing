package be.kdg.prog6.order.port.in;

import java.math.BigDecimal;

public record AddDishToBasketCommand(
        String customerId,
        String restaurantId,
        String dishId,
        String dishName,
        BigDecimal unitPrice,
        int quantity
) {
}