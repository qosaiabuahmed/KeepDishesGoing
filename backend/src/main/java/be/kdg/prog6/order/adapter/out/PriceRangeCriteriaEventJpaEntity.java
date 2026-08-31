package be.kdg.prog6.order.adapter.out;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "price_range_criteria_events", schema = "customer")
public class PriceRangeCriteriaEventJpaEntity {

    @Id
    private UUID eventId;

    @Column(nullable = false)
    private BigDecimal lowThreshold;

    @Column(nullable = false)
    private BigDecimal mediumThreshold;

    @Column(nullable = false)
    private BigDecimal highThreshold;

    @Column(nullable = false)
    private LocalDateTime effectiveDate;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    public PriceRangeCriteriaEventJpaEntity() {
    }

    public PriceRangeCriteriaEventJpaEntity(
            UUID eventId,
            BigDecimal lowThreshold,
            BigDecimal mediumThreshold,
            BigDecimal highThreshold,
            LocalDateTime effectiveDate,
            LocalDateTime occurredAt) {
        this.eventId = eventId;
        this.lowThreshold = lowThreshold;
        this.mediumThreshold = mediumThreshold;
        this.highThreshold = highThreshold;
        this.effectiveDate = effectiveDate;
        this.occurredAt = occurredAt;
    }

    public UUID getEventId() {
        return eventId;
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

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}