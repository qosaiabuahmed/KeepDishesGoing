package be.kdg.prog6.restaurant.port.in;

import be.kdg.prog6.restaurant.adapter.in.request.CreateAddressRequest;
import org.springframework.util.Assert;

import java.util.List;

public record CreateRestaurantCommand(
        String ownerId,
        String name,
        String cuisine,
        CreateAddressRequest address,
        String contactEmail,
        List<String> imageUrls,
        int defaultPrepTimeMinutes,
        String openingHours
) {
    public CreateRestaurantCommand {
        Assert.hasLength(ownerId, "Owner ID cannot be empty");
        Assert.hasLength(name, "Restaurant name cannot be empty");
        Assert.hasLength(cuisine, "Cuisine type cannot be empty");
        Assert.notNull(address, "Address cannot be null");
        Assert.hasLength(contactEmail, "Contact email cannot be empty");
        Assert.isTrue(defaultPrepTimeMinutes > 0, "Default prep time must be greater than zero");
        Assert.notNull(openingHours, "Opening hours cannot be null");
    }
}