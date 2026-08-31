package be.kdg.prog6.restaurant.port.in;

import org.springframework.util.Assert;

public record PublishDishCommand(
        String restaurantId,
        String dishId
) {
    public PublishDishCommand {
        Assert.hasLength(restaurantId, "Restaurant ID cannot be empty");
        Assert.hasLength(dishId, "Dish ID cannot be empty");
    }
}