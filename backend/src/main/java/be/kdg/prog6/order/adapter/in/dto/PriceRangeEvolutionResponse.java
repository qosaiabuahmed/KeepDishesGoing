package be.kdg.prog6.order.adapter.in.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceRangeEvolutionResponse(
        LocalDateTime date,
        String priceRange,
        BigDecimal averageMenuPrice,
        BigDecimal lowThreshold,
        BigDecimal mediumThreshold,
        BigDecimal highThreshold
) {}