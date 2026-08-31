package be.kdg.prog6.common.events;

import be.kdg.prog6.common.events.external.AddressDto;
import be.kdg.prog6.common.events.external.Coordinates;
import org.springframework.modulith.events.Externalized;

import java.time.LocalDateTime;
import java.util.UUID;

@Externalized("kdg.events::#{'restaurant.' + #this.restaurantId + '.order.accepted.v1'}")
public record OrderAcceptedEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        UUID restaurantId,
        UUID orderId,
        AddressDto pickupAddress,
        Coordinates pickupCoordinates,
        AddressDto dropoffAddress,
        Coordinates dropoffCoordinates
) {}