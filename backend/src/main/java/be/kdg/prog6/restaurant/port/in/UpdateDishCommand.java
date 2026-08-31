package be.kdg.prog6.restaurant.port.in;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

public record UpdateDishCommand(
        String restaurantId,
        String dishId,
        String name,
        String type,
        List<String> foodTags,
        String description,
        BigDecimal price,
        String imageUrl
) {
    public UpdateDishCommand {
        Assert.hasLength(restaurantId, "Restaurant ID cannot be empty");
        Assert.hasLength(dishId, "Dish ID cannot be empty");
        Assert.hasLength(name, "Dish name cannot be empty");
        Assert.hasLength(type, "Dish type cannot be empty");
        Assert.notNull(foodTags, "Food tags cannot be null");
        Assert.hasLength(description, "Description cannot be empty");
        Assert.notNull(price, "Price cannot be null");
        Assert.isTrue(price.compareTo(BigDecimal.ZERO) > 0, "Price must be positive");
        Assert.hasLength(imageUrl, "Image URL cannot be empty");
    }
}