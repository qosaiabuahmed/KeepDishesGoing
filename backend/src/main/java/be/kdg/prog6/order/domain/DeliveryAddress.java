package be.kdg.prog6.order.domain;

public record DeliveryAddress(
        String street,
        String number,
        String city,
        String postalCode,
        String country
) {
    public DeliveryAddress {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Number cannot be empty");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Postal code cannot be empty");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }
    }

    public String toFullAddress() {
        return String.format("%s %s, %s %s, %s", street, number, postalCode, city, country);
    }
}