package be.kdg.prog6.common.events;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;

public record DishUpdatedEvent(
        DishId dishId,
        RestaurantId restaurantId
) {}