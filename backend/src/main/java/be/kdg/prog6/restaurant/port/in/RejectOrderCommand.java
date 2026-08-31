package be.kdg.prog6.restaurant.port.in;

public record RejectOrderCommand(
        String orderId,
        String restaurantId,
        String rejectionReason
) {
}