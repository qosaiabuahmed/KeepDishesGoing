package be.kdg.prog6.restaurant.port.in;

import org.springframework.util.Assert;

public record PublishAllPendingChangesCommand(
        String restaurantId
) {
    public PublishAllPendingChangesCommand {
        Assert.hasLength(restaurantId, "Restaurant ID cannot be empty");
    }
}