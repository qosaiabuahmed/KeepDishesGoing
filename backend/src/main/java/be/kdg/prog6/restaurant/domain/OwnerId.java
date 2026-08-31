package be.kdg.prog6.restaurant.domain;

import java.util.UUID;

public record OwnerId(UUID value) {
    public OwnerId {
        if (value == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }
    }

    public static OwnerId of(String uuidString) {
        return new OwnerId(UUID.fromString(uuidString));
    }

    public static OwnerId newId() {
        return new OwnerId(UUID.randomUUID());
    }
}