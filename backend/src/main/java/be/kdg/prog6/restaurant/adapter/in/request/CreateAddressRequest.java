package be.kdg.prog6.restaurant.adapter.in.request;

public record CreateAddressRequest(
        String street,
        String number,
        String postalCode,
        String city,
        String country
) {}
