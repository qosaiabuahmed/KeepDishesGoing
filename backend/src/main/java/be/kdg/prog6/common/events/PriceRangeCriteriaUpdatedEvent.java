package be.kdg.prog6.common.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceRangeCriteriaUpdatedEvent(
        BigDecimal lowThreshold,
        BigDecimal mediumThreshold,
        BigDecimal highThreshold,
        LocalDateTime effectiveDate,
        LocalDateTime occurredAt
) {}