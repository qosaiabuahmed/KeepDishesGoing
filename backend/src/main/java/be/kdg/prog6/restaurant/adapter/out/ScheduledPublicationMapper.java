package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.ScheduledPublication;
import be.kdg.prog6.restaurant.domain.ScheduledPublicationId;
import be.kdg.prog6.restaurant.domain.ScheduledPublicationStatus;
import org.springframework.stereotype.Component;

@Component
public class ScheduledPublicationMapper {

    public ScheduledPublicationJpaEntity toJpaEntity(ScheduledPublication domain) {
        return new ScheduledPublicationJpaEntity(
                domain.getId().value(),
                domain.getRestaurantId().value(),
                domain.getDishIdsToPublish().stream()
                        .map(DishId::value)
                        .toList(),
                domain.getDishIdsToUnpublish().stream()
                        .map(DishId::value)
                        .toList(),
                domain.getScheduledTime(),
                domain.getStatus().name(),
                domain.getExecutedAt()
        );
    }

    public ScheduledPublication toDomain(ScheduledPublicationJpaEntity jpa) {
        return new ScheduledPublication(
                new ScheduledPublicationId(jpa.getId()),
                new RestaurantId(jpa.getRestaurantId()),
                jpa.getDishIdsToPublish().stream()
                        .map(DishId::new)
                        .toList(),
                jpa.getDishIdsToUnpublish().stream()
                        .map(DishId::new)
                        .toList(),
                jpa.getScheduledTime(),
                ScheduledPublicationStatus.valueOf(jpa.getStatus()),
                jpa.getExecutedAt()
        );
    }
}