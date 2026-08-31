package be.kdg.prog6.restaurant.port.out;

import be.kdg.prog6.restaurant.domain.ScheduledPublication;

import java.time.Instant;
import java.util.List;

public interface LoadScheduledPublicationPort {
    List<ScheduledPublication> findPendingPublicationsReadyForExecution(Instant now);
}