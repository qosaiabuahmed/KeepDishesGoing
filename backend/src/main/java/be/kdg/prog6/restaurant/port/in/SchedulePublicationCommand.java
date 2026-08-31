package be.kdg.prog6.restaurant.port.in;

import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;

public record SchedulePublicationCommand(
        String restaurantId,
        List<String> dishIdsToPublish,
        List<String> dishIdsToUnpublish,
        Instant scheduledTime
) {
    public SchedulePublicationCommand {
        Assert.hasLength(restaurantId, "Restaurant ID cannot be empty");
        Assert.notNull(scheduledTime, "Scheduled time cannot be null");
        Assert.notNull(dishIdsToPublish, "dishIdsToPublish cannot be null");
        Assert.notNull(dishIdsToUnpublish, "dishIdsToUnpublish cannot be null");
    }
}