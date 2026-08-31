package be.kdg.prog6.order.port.out;

import be.kdg.prog6.common.domain.BasketId;
import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.order.domain.Basket;

import java.util.Optional;

public interface LoadBasketPort {
    Optional<Basket> loadById(BasketId basketId);
    Optional<Basket> loadByCustomerId(CustomerId customerId);
}