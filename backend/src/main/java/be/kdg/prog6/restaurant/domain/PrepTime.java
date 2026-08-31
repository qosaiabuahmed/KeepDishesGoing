package be.kdg.prog6.restaurant.domain;

public record PrepTime(int minutes) {
    public PrepTime {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Prep time must be positive");
        }
        if (minutes > 300) {
            throw new IllegalArgumentException("Prep time too long");
        }
    }
}