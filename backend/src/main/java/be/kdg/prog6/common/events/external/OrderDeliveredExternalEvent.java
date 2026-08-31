package be.kdg.prog6.common.events.external;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDeliveredExternalEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        UUID restaurantId,
        UUID orderId
) {}