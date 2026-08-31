package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.DishProjection;
import be.kdg.prog6.order.port.in.GetDishesForCustomerQuery;
import be.kdg.prog6.order.port.out.LoadDishProjectionPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDishesForCustomerQueryImpl implements GetDishesForCustomerQuery {

    private final LoadDishProjectionPort loadDishProjectionPort;

    public GetDishesForCustomerQueryImpl(LoadDishProjectionPort loadDishProjectionPort) {
        this.loadDishProjectionPort = loadDishProjectionPort;
    }

    @Override
    public List<DishProjection> getAvailableDishes(RestaurantId restaurantId) {
        return loadDishProjectionPort.loadByRestaurantId(restaurantId);
    }
}