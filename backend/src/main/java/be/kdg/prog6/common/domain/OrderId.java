package be.kdg.prog6.common.domain;

import java.util.UUID;

public record OrderId(UUID value) {
    public OrderId {
        if (value == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
    }

    public static OrderId of(String uuidString) {
        return new OrderId(UUID.fromString(uuidString));
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }
}