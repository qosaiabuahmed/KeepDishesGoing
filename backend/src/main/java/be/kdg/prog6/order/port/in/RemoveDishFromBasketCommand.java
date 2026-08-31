package be.kdg.prog6.order.port.in;

public record RemoveDishFromBasketCommand(
        String customerId,
        String dishId
) {
}