package be.kdg.prog6.order.domain;

import java.time.LocalDateTime;

public record CourierLocation(
        Double latitude,
        Double longitude,
        LocalDateTime lastUpdated
) {
    public CourierLocation {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Latitude and longitude cannot be null");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        if (lastUpdated == null) {
            lastUpdated = LocalDateTime.now();
        }
    }
}