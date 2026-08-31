package be.kdg.prog6.restaurant.adapter.in.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DishDto(
        UUID id,
        String name,
        String type,
        List<String> foodTags,
        String description,
        BigDecimal price,
        String imageUrl,
        String draftName,
        String draftType,
        List<String> draftFoodTags,
        String draftDescription,
        BigDecimal draftPrice,
        String draftImageUrl,
        String publishStatus,
        String stockStatus,
        boolean hasPendingChanges,
        boolean isAvailableForOrder,
        boolean isVisible
) {}