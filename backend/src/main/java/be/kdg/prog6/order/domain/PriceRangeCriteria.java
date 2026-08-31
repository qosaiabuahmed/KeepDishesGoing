package be.kdg.prog6.order.domain;

import be.kdg.prog6.common.events.PriceRangeCriteriaUpdatedEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriceRangeCriteria {
    private final BigDecimal lowThreshold;
    private final BigDecimal mediumThreshold;
    private final BigDecimal highThreshold;
    private final LocalDateTime effectiveDate;
    private final List<Object> domainEvents = new ArrayList<>();

    public PriceRangeCriteria(
            BigDecimal lowThreshold,
            BigDecimal mediumThreshold,
            BigDecimal highThreshold,
            LocalDateTime effectiveDate) {
        validateThresholds(lowThreshold, mediumThreshold, highThreshold);
        this.lowThreshold = lowThreshold;
        this.mediumThreshold = mediumThreshold;
        this.highThreshold = highThreshold;
        this.effectiveDate = effectiveDate;

        domainEvents.add(new PriceRangeCriteriaUpdatedEvent(
                lowThreshold,
                mediumThreshold,
                highThreshold,
                effectiveDate,
                LocalDateTime.now()
        ));
    }

    public static PriceRangeCriteria getDefault() {
        return new PriceRangeCriteria(
                new BigDecimal("10"),
                new BigDecimal("30"),
                new BigDecimal("60"),
                LocalDateTime.now()
        );
    }

    private void validateThresholds(BigDecimal low, BigDecimal medium, BigDecimal high) {
        if (low.compareTo(BigDecimal.ZERO) < 0 || medium.compareTo(BigDecimal.ZERO) < 0 || high.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("All thresholds must be positive");
        }
        if (low.compareTo(medium) >= 0) {
            throw new IllegalArgumentException("Low threshold must be less than medium threshold");
        }
        if (medium.compareTo(high) >= 0) {
            throw new IllegalArgumentException("Medium threshold must be less than high threshold");
        }
    }

    public String calculatePriceRange(BigDecimal averagePrice) {
        System.out.println("DEBUG: calculatePriceRange called with avgPrice=" + averagePrice +
                         ", lowThreshold=" + lowThreshold +
                         ", mediumThreshold=" + mediumThreshold +
                         ", highThreshold=" + highThreshold);

        if (averagePrice.compareTo(lowThreshold) <= 0) {
            System.out.println("DEBUG: Returning CHEAP");
            return "CHEAP";
        } else if (averagePrice.compareTo(mediumThreshold) <= 0) {
            System.out.println("DEBUG: Returning REGULAR");
            return "REGULAR";
        } else if (averagePrice.compareTo(highThreshold) <= 0) {
            System.out.println("DEBUG: Returning EXPENSIVE");
            return "EXPENSIVE";
        } else {
            System.out.println("DEBUG: Returning PREMIUM");
            return "PREMIUM";
        }
    }

    public BigDecimal getLowThreshold() {
        return lowThreshold;
    }

    public BigDecimal getMediumThreshold() {
        return mediumThreshold;
    }

    public BigDecimal getHighThreshold() {
        return highThreshold;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}