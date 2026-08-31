package be.kdg.prog6.order.core;

import be.kdg.prog6.order.domain.RestaurantProjection;
import be.kdg.prog6.order.port.in.GetRestaurantsForCustomerQuery;
import be.kdg.prog6.order.port.out.LoadRestaurantProjectionPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRestaurantsForCustomerQueryImpl implements GetRestaurantsForCustomerQuery {

    private final LoadRestaurantProjectionPort loadRestaurantProjectionPort;

    public GetRestaurantsForCustomerQueryImpl(LoadRestaurantProjectionPort loadRestaurantProjectionPort) {
        this.loadRestaurantProjectionPort = loadRestaurantProjectionPort;
    }

    @Override
    public List<RestaurantProjection> getAllRestaurants() {
        return loadRestaurantProjectionPort.loadAll();
    }
}