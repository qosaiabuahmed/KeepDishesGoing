package be.kdg.prog6.common.events;

import be.kdg.prog6.common.domain.RestaurantId;

import java.time.LocalDateTime;
import java.util.List;

public record RestaurantCreatedEvent(
        RestaurantId restaurantId,
        String name,
        String cuisine,
        String street,
        String number,
        String postalCode,
        String city,
        String country,
        String contactEmail,
        List<String> imageUrls,
        boolean isOpen,
        LocalDateTime occurredAt
) {}