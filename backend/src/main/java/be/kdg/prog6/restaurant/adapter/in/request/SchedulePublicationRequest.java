package be.kdg.prog6.restaurant.adapter.in.request;

import java.time.Instant;
import java.util.List;

public record SchedulePublicationRequest(
        List<String> dishIdsToPublish,
        List<String> dishIdsToUnpublish,
        Instant scheduledTime
) {
}