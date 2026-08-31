package be.kdg.prog6.restaurant.port.in;

import be.kdg.prog6.restaurant.domain.ScheduledPublicationId;

public interface SchedulePublicationUseCase {
    ScheduledPublicationId schedulePublication(SchedulePublicationCommand command);
}
