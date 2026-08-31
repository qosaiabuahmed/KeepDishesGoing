package be.kdg.prog6.common.events;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;

import java.time.LocalDateTime;

public record DishUnpublishedEvent(
        DishId dishId,
        RestaurantId restaurantId,
        LocalDateTime occurredAt
) {}