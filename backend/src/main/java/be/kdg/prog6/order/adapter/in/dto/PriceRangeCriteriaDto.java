package be.kdg.prog6.order.adapter.in.dto;

import java.math.BigDecimal;

public record PriceRangeCriteriaDto(
        BigDecimal lowThreshold,
        BigDecimal mediumThreshold,
        BigDecimal highThreshold
) {}