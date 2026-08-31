package be.kdg.prog6.order.port.in;

import be.kdg.prog6.order.domain.Basket;

public interface GetBasketQuery {
    Basket getBasket(String customerId);
}