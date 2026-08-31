package be.kdg.prog6.order.port.out;

import be.kdg.prog6.order.domain.Basket;

public interface SaveBasketPort {
    Basket save(Basket basket);
}