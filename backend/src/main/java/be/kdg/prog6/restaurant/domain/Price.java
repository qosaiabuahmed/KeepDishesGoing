package be.kdg.prog6.restaurant.domain;

import java.math.BigDecimal;

public record Price(BigDecimal amount) {
    public Price {
        if (amount == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            throw new IllegalArgumentException("Price too high");
        }
    }

    public static Price of(double amount) {
        return new Price(BigDecimal.valueOf(amount));
    }
}