package be.kdg.prog6.order.adapter.in.request;

import java.math.BigDecimal;

public record UpdatePriceRangeCriteriaRequest(
        BigDecimal lowThreshold,
        BigDecimal mediumThreshold,
        BigDecimal highThreshold
) {}