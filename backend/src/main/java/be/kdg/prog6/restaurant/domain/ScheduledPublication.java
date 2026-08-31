package be.kdg.prog6.restaurant.domain;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduledPublication {
    private final ScheduledPublicationId id;
    private final RestaurantId restaurantId;
    private final List<DishId> dishIdsToPublish;
    private final List<DishId> dishIdsToUnpublish;
    private final Instant scheduledTime;
    private ScheduledPublicationStatus status;
    private Instant executedAt;

    public ScheduledPublication(
            ScheduledPublicationId id,
            RestaurantId restaurantId,
            List<DishId> dishIdsToPublish,
            List<DishId> dishIdsToUnpublish,
            Instant scheduledTime,
            ScheduledPublicationStatus status,
            Instant executedAt) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.dishIdsToPublish = new ArrayList<>(dishIdsToPublish);
        this.dishIdsToUnpublish = new ArrayList<>(dishIdsToUnpublish);
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.executedAt = executedAt;
    }

    public static ScheduledPublication createNew(
            RestaurantId restaurantId,
            List<DishId> dishIdsToPublish,
            List<DishId> dishIdsToUnpublish,
            Instant scheduledTime) {

        if (dishIdsToPublish.isEmpty() && dishIdsToUnpublish.isEmpty()) {
            throw new IllegalArgumentException("Must specify at least one dish to publish or unpublish");
        }

        return new ScheduledPublication(
                ScheduledPublicationId.newId(),
                restaurantId,
                dishIdsToPublish,
                dishIdsToUnpublish,
                scheduledTime,
                ScheduledPublicationStatus.PENDING,
                null
        );
    }

    public boolean isReadyForExecution() {
        return status == ScheduledPublicationStatus.PENDING
                && Instant.now().isAfter(scheduledTime);
    }

    public void markAsExecuted() {
        if (status != ScheduledPublicationStatus.PENDING) {
            throw new IllegalStateException("Can only execute pending publications");
        }
        this.status = ScheduledPublicationStatus.EXECUTED;
        this.executedAt = Instant.now();
    }

    public void cancel() {
        if (status != ScheduledPublicationStatus.PENDING) {
            throw new IllegalStateException("Can only cancel pending publications");
        }
        this.status = ScheduledPublicationStatus.CANCELLED;
    }

    public ScheduledPublicationId getId() { return id; }
    public RestaurantId getRestaurantId() { return restaurantId; }
    public List<DishId> getDishIdsToPublish() { return Collections.unmodifiableList(dishIdsToPublish); }
    public List<DishId> getDishIdsToUnpublish() { return Collections.unmodifiableList(dishIdsToUnpublish); }
    public Instant getScheduledTime() { return scheduledTime; }
    public ScheduledPublicationStatus getStatus() { return status; }
    public Instant getExecutedAt() { return executedAt; }
}