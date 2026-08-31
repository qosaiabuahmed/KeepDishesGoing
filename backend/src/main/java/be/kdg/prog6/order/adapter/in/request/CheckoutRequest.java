package be.kdg.prog6.order.adapter.in.request;

public record CheckoutRequest(
        String customerName,
        String contactEmail,
        String street,
        String number,
        String city,
        String postalCode,
        String country,
        String currency
) {}