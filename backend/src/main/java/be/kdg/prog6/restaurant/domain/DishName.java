package be.kdg.prog6.restaurant.domain;

public record DishName(String value) {
    public DishName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Dish name cannot be empty");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("Dish name too long");
        }
    }
}