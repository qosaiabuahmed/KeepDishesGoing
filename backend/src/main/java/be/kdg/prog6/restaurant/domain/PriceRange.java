package be.kdg.prog6.restaurant.domain;

import java.math.BigDecimal;

public enum PriceRange {
    CHEAP("€", BigDecimal.ZERO, new BigDecimal("10")),
    REGULAR("€€", new BigDecimal("11"), new BigDecimal("30")),
    EXPENSIVE("€€€", new BigDecimal("31"), new BigDecimal("60")),
    PREMIUM("€€€€", new BigDecimal("61"), new BigDecimal("100000"));

    private final String symbol;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;

    PriceRange(String symbol, BigDecimal minPrice, BigDecimal maxPrice) {
        this.symbol = symbol;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public static PriceRange fromAveragePrice(BigDecimal averagePrice) {
        for (PriceRange range : values()) {
            if (averagePrice.compareTo(range.minPrice) >= 0 && averagePrice.compareTo(range.maxPrice) <= 0) {
                return range;
            }
        }
        return PREMIUM;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
}