package be.kdg.prog6.common.events.external;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderPickedUpExternalEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        UUID restaurantId,
        UUID orderId
) {}