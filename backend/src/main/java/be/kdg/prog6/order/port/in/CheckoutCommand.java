package be.kdg.prog6.order.port.in;

public record CheckoutCommand(
        String customerId,
        String customerName,
        String contactEmail,
        String street,
        String number,
        String city,
        String postalCode,
        String country,
        String currency
) {
}