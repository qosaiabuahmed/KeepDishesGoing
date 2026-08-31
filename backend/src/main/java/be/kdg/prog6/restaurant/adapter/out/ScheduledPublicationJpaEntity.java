package be.kdg.prog6.restaurant.adapter.out;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "scheduled_publications", schema = "restaurant")
public class ScheduledPublicationJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "scheduled_publication_dishes_to_publish",
            schema = "restaurant",
            joinColumns = @JoinColumn(name = "scheduled_publication_id")
    )
    @Column(name = "dish_id")
    private List<UUID> dishIdsToPublish = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "scheduled_publication_dishes_to_unpublish",
            schema = "restaurant",
            joinColumns = @JoinColumn(name = "scheduled_publication_id")
    )
    @Column(name = "dish_id")
    private List<UUID> dishIdsToUnpublish = new ArrayList<>();

    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "executed_at")
    private Instant executedAt;

    protected ScheduledPublicationJpaEntity() {
    }

    public ScheduledPublicationJpaEntity(
            UUID id,
            UUID restaurantId,
            List<UUID> dishIdsToPublish,
            List<UUID> dishIdsToUnpublish,
            Instant scheduledTime,
            String status,
            Instant executedAt) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.dishIdsToPublish = new ArrayList<>(dishIdsToPublish);
        this.dishIdsToUnpublish = new ArrayList<>(dishIdsToUnpublish);
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.executedAt = executedAt;
    }

    public UUID getId() { return id; }

    public UUID getRestaurantId() { return restaurantId; }

    public List<UUID> getDishIdsToPublish() { return dishIdsToPublish; }

    public List<UUID> getDishIdsToUnpublish() { return dishIdsToUnpublish; }

    public Instant getScheduledTime() { return scheduledTime; }

    public String getStatus() { return status; }

    public Instant getExecutedAt() { return executedAt; }
}