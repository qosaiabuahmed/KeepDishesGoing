package be.kdg.prog6.order.port.in;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdatePriceRangeCriteriaCommand(
        BigDecimal lowThreshold,
        BigDecimal mediumThreshold,
        BigDecimal highThreshold,
        LocalDateTime effectiveDate
) {
    public UpdatePriceRangeCriteriaCommand {
        if (lowThreshold == null || mediumThreshold == null || highThreshold == null) {
            throw new IllegalArgumentException("All thresholds must be provided");
        }
        if (lowThreshold.compareTo(BigDecimal.ZERO) < 0 ||
            mediumThreshold.compareTo(BigDecimal.ZERO) < 0 ||
            highThreshold.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("All thresholds must be positive");
        }
        if (lowThreshold.compareTo(mediumThreshold) >= 0) {
            throw new IllegalArgumentException("Low threshold must be less than medium threshold");
        }
        if (mediumThreshold.compareTo(highThreshold) >= 0) {
            throw new IllegalArgumentException("Medium threshold must be less than high threshold");
        }
        if (effectiveDate == null) {
            throw new IllegalArgumentException("Effective date must be provided");
        }
    }
}