package be.kdg.prog6.common.events.external;

public record AddressDto(
        String street,
        String number,
        String postalCode,
        String city
) {}