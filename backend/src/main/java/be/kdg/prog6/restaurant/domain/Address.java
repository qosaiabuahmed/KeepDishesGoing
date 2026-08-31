package be.kdg.prog6.restaurant.domain;

public record Address(
        String street,
        String number,
        String postalCode,
        String city,
        String country
) {
    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }
    }

    public String fullAddress() {
        return String.format("%s %s, %s %s, %s",
                street, number, postalCode, city, country);
    }
}