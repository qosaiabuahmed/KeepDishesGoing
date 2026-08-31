package be.kdg.prog6.restaurant.adapter.in.request;

import java.math.BigDecimal;
import java.util.List;

public record CreateDishRequest(
        String name,
        String type,
        List<String> foodTags,
        String description,
        BigDecimal price,
        String imageUrl
) {}