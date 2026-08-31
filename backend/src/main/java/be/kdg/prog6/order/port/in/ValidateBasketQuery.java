package be.kdg.prog6.order.port.in;

import be.kdg.prog6.order.domain.BasketValidationResult;

public interface ValidateBasketQuery {
    BasketValidationResult validateBasket(String customerId);
}