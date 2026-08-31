package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.ScheduledPublication;
import be.kdg.prog6.restaurant.domain.ScheduledPublicationId;
import be.kdg.prog6.restaurant.port.in.SchedulePublicationCommand;
import be.kdg.prog6.restaurant.port.in.SchedulePublicationUseCase;
import be.kdg.prog6.restaurant.port.out.SaveScheduledPublicationPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SchedulePublicationUseCaseImpl implements SchedulePublicationUseCase {

    private final SaveScheduledPublicationPort saveScheduledPublicationPort;

    public SchedulePublicationUseCaseImpl(SaveScheduledPublicationPort saveScheduledPublicationPort) {
        this.saveScheduledPublicationPort = saveScheduledPublicationPort;
    }

    @Override
    public ScheduledPublicationId schedulePublication(SchedulePublicationCommand command) {
        RestaurantId restaurantId = RestaurantId.of(command.restaurantId());

        List<DishId> dishIdsToPublish = command.dishIdsToPublish().stream()
                .map(DishId::of)
                .toList();

        List<DishId> dishIdsToUnpublish = command.dishIdsToUnpublish().stream()
                .map(DishId::of)
                .toList();

        ScheduledPublication scheduledPublication = ScheduledPublication.createNew(
                restaurantId,
                dishIdsToPublish,
                dishIdsToUnpublish,
                command.scheduledTime()
        );

        saveScheduledPublicationPort.save(scheduledPublication);

        return scheduledPublication.getId();
    }
}