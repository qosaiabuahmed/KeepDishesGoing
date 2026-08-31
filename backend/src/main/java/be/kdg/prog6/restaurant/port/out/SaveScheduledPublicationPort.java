package be.kdg.prog6.restaurant.port.out;

import be.kdg.prog6.restaurant.domain.ScheduledPublication;

public interface SaveScheduledPublicationPort {
    ScheduledPublication save(ScheduledPublication scheduledPublication);
}