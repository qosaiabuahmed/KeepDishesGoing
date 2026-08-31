package be.kdg.prog6.common.events;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DishPublishedEvent(
        DishId dishId,
        RestaurantId restaurantId,
        String dishName,
        String dishType,
        List<String> foodTags,
        String description,
        BigDecimal price,
        String imageUrl,
        LocalDateTime occurredAt
) {}