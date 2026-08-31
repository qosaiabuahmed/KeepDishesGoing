package be.kdg.prog6.common.events;

import be.kdg.prog6.common.domain.RestaurantId;
import java.time.LocalDateTime;

public record RestaurantClosedEvent(
        RestaurantId restaurantId,
        LocalDateTime occurredOn
) {}