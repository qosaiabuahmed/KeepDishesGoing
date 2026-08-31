package be.kdg.prog6.common.domain;

import java.util.UUID;

public record RestaurantId(UUID value) {
    public RestaurantId {
        if (value == null) {
            throw new IllegalArgumentException("Restaurant ID cannot be null");
        }
    }

    public static RestaurantId of(String uuidString) {
        return new RestaurantId(UUID.fromString(uuidString));
    }

    public static RestaurantId newId() {
        return new RestaurantId(UUID.randomUUID());
    }
}