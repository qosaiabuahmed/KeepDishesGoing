package be.kdg.prog6.restaurant.adapter.in.request;

import java.util.List;

public record CreateRestaurantRequest(
        String name,
        String cuisine,
        CreateAddressRequest address,
        String contactEmail,
        List<String> imageUrls,
        int defaultPrepTimeMinutes,
        String openingHours
) {}

