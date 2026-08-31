package be.kdg.prog6.restaurant.domain;

public record RestaurantName(String value) {
    public RestaurantName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Restaurant name cannot be empty");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("Restaurant name too long");
        }
    }
}