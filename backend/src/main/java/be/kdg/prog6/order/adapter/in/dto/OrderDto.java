package be.kdg.prog6.order.adapter.in.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        String orderId,
        String customerId,
        String restaurantId,
        String customerName,
        String contactEmail,
        DeliveryAddressDto deliveryAddress,
        List<OrderItemDto> items,
        BigDecimal totalAmount,
        String paymentTransactionId,
        LocalDateTime orderTime,
        String status,
        CourierLocationDto courierLocation
) {}