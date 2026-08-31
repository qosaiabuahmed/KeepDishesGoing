package be.kdg.prog6.order.adapter.in.dto;

public record DeliveryAddressDto(
        String street,
        String number,
        String city,
        String postalCode,
        String country
) {}