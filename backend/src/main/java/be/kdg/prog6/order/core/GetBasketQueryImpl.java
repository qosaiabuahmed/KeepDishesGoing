package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.order.domain.Basket;
import be.kdg.prog6.order.port.in.GetBasketQuery;
import be.kdg.prog6.order.port.out.LoadBasketPort;
import org.springframework.stereotype.Service;

@Service
public class GetBasketQueryImpl implements GetBasketQuery {

    private final LoadBasketPort loadBasketPort;

    public GetBasketQueryImpl(LoadBasketPort loadBasketPort) {
        this.loadBasketPort = loadBasketPort;
    }

    @Override
    public Basket getBasket(String customerId) {
        CustomerId id = CustomerId.of(customerId);

        return loadBasketPort.loadByCustomerId(id)
                .orElseGet(() -> Basket.createNew(id));
    }
}