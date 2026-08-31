package be.kdg.prog6.restaurant.adapter.in.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderProjectionDto(
        String orderId,
        String restaurantId,
        String customerId,
        String customerName,
        String contactEmail,
        DeliveryAddressDto deliveryAddress,
        List<OrderItemDto> items,
        BigDecimal totalAmount,
        String paymentTransactionId,
        LocalDateTime orderTime,
        String status,
        String rejectionReason,
        LocalDateTime decisionTime
) {
    public record DeliveryAddressDto(
            String street,
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