package be.kdg.prog6.restaurant.domain;

import java.util.Objects;
import java.util.UUID;

public record MenuId(UUID value) {
    public MenuId {
        Objects.requireNonNull(value, "Menu ID cannot be null");
    }

    public static MenuId of(String id) {
        return new MenuId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}