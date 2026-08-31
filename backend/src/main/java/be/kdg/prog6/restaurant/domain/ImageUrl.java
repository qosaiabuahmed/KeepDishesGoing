package be.kdg.prog6.restaurant.domain;

public record ImageUrl(String value) {
    public ImageUrl {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be empty");
        }
        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            throw new IllegalArgumentException("Image URL must start with http:// or https://");
        }
    }
}