package be.kdg.prog6.common.domain;

import java.util.UUID;

public record CustomerId(UUID value) {
    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
    }

    public static CustomerId of(String uuidString) {
        return new CustomerId(UUID.fromString(uuidString));
    }

    public static CustomerId newId() {
        return new CustomerId(UUID.randomUUID());
    }
}