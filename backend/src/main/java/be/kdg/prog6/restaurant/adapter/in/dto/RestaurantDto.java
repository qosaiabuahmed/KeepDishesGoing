package be.kdg.prog6.restaurant.adapter.in.dto;

import java.util.List;
import java.util.UUID;

public record RestaurantDto(
        UUID id,
        String name,
        String cuisine,
        AddressDto address,
        String contactEmail,
        List<String> imageUrls,
        int defaultPrepTimeMinutes,
        OpeningHoursDto openingHours,
        boolean isOpen,
        int availableDishesCount
) {
    public record AddressDto(
            String street,
            String number,
            String postalCode,
            String city,
            String country
    ) {
        public String fullAddress() {
            return String.format("%s %s, %s %s, %s", street, number, postalCode, city, country);
        }
    }

    public record OpeningHoursDto(
            String schedule
    ) {}
}