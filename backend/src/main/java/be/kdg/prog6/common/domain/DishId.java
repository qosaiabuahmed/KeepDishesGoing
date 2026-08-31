package be.kdg.prog6.common.domain;

import java.util.UUID;

public record DishId(UUID value) {
    public DishId {
        if (value == null) {
            throw new IllegalArgumentException("Dish ID cannot be null");
        }
    }

    public static DishId of(String uuidString) {
        return new DishId(UUID.fromString(uuidString));
    }

    public static DishId newId() {
        return new DishId(UUID.randomUUID());
    }
}