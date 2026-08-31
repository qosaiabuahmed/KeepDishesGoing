package be.kdg.prog6.restaurant.port.in;

public record MarkOrderReadyCommand(
        String orderId,
        String restaurantId
) {}