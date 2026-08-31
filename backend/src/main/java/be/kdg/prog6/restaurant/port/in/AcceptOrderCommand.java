package be.kdg.prog6.restaurant.port.in;

public record AcceptOrderCommand(
        String orderId,
        String restaurantId
) {
}