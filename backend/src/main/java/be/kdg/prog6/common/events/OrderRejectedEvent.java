package be.kdg.prog6.common.events;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;

import java.time.LocalDateTime;

public record OrderRejectedEvent(
        OrderId orderId,
        RestaurantId restaurantId,
        String rejectionReason,
        LocalDateTime occurredAt
) {}