package be.kdg.prog6.restaurant.domain;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

public record ScheduledPublicationId(UUID value) {
    public ScheduledPublicationId {
        Assert.notNull(value, "ScheduledPublicationId value cannot be null");
    }

    public static ScheduledPublicationId of(String value) {
        return new ScheduledPublicationId(UUID.fromString(value));
    }

    public static ScheduledPublicationId newId() {
        return new ScheduledPublicationId(UUID.randomUUID());
    }
}