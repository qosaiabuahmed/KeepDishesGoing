package be.kdg.prog6.common.events.external;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAcceptedExternalEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        UUID restaurantId,
        UUID orderId,
        AddressDto pickupAddress,
        Coordinates pickupCoordinates,
        AddressDto dropoffAddress,
        Coordinates dropoffCoordinates
) {}