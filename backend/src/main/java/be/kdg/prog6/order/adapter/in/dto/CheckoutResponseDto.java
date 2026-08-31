package be.kdg.prog6.order.adapter.in.dto;

public record CheckoutResponseDto(
        String orderId,
        String message,
        String trackingUrl
) {}