package be.kdg.prog6.common.domain;

import java.util.UUID;

public record BasketId(UUID value) {
    public BasketId {
        if (value == null) {
            throw new IllegalArgumentException("Basket ID cannot be null");
        }
    }

    public static BasketId of(String uuidString) {
        return new BasketId(UUID.fromString(uuidString));
    }

    public static BasketId newId() {
        return new BasketId(UUID.randomUUID());
    }
}