package be.kdg.prog6.restaurant.domain;

public record Description(String value) {
    public Description {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (value.length() > 500) {
            throw new IllegalArgumentException("Description too long");
        }
    }
}