package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.order.domain.Basket;
import be.kdg.prog6.order.domain.BasketValidationResult;
import be.kdg.prog6.order.port.in.ValidateBasketQuery;
import be.kdg.prog6.order.port.out.LoadBasketPort;
import be.kdg.prog6.order.port.out.LoadDishProjectionPort;
import org.springframework.stereotype.Service;

@Service
public class ValidateBasketQueryImpl implements ValidateBasketQuery {

    private final LoadBasketPort loadBasketPort;
    private final LoadDishProjectionPort loadDishProjectionPort;

    public ValidateBasketQueryImpl(
            LoadBasketPort loadBasketPort,
            LoadDishProjectionPort loadDishProjectionPort) {
        this.loadBasketPort = loadBasketPort;
        this.loadDishProjectionPort = loadDishProjectionPort;
    }

    @Override
    public BasketValidationResult validateBasket(String customerId) {
        CustomerId id = CustomerId.of(customerId);

        Basket basket = loadBasketPort.loadByCustomerId(id)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found"));

        return basket.validate(loadDishProjectionPort::loadById);
    }
}