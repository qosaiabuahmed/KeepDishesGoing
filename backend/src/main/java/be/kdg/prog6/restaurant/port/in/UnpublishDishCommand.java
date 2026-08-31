package be.kdg.prog6.restaurant.port.in;

import org.springframework.util.Assert;

public record UnpublishDishCommand(
        String restaurantId,
        String dishId
) {
    public UnpublishDishCommand {
        Assert.hasLength(restaurantId, "Restaurant ID cannot be empty");
        Assert.hasLength(dishId, "Dish ID cannot be empty");
    }
}