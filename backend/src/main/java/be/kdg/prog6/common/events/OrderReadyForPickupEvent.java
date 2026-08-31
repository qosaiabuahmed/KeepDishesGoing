package be.kdg.prog6.common.events;

import org.springframework.modulith.events.Externalized;

import java.time.LocalDateTime;
import java.util.UUID;

@Externalized("kdg.events::#{'restaurant.' + #this.restaurantId + '.order.ready.v1'}")
public record OrderReadyForPickupEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        UUID restaurantId,
        UUID orderId
) {}