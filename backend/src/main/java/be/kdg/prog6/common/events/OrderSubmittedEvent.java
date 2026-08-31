package be.kdg.prog6.common.events;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderSubmittedEvent(
        OrderId orderId,
        RestaurantId restaurantId,
        CustomerId customerId,
        String customerName,
        String contactEmail,
        DeliveryAddressDto deliveryAddress,
        List<OrderItemDto> items,
        BigDecimal totalAmount,
        String paymentTransactionId,
        LocalDateTime orderTime,
        LocalDateTime occurredAt
) {
    public record DeliveryAddressDto(
            String street,
            String number,
            String city,
            String postalCode,
            String country
    ) {}

    public record OrderItemDto(
            String dishId,
            String dishName,
            BigDecimal unitPrice,
            int quantity,
            BigDecimal totalPrice
    ) {}
}