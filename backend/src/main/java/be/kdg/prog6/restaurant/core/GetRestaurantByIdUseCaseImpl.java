package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.Restaurant;
import be.kdg.prog6.restaurant.port.in.GetRestaurantByIdUseCase;
import be.kdg.prog6.restaurant.port.out.LoadRestaurantPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GetRestaurantByIdUseCaseImpl implements GetRestaurantByIdUseCase {

    private final LoadRestaurantPort loadRestaurantPort;

    public GetRestaurantByIdUseCaseImpl(LoadRestaurantPort loadRestaurantPort) {
        this.loadRestaurantPort = loadRestaurantPort;
    }

    @Override
    public Optional<Restaurant> getRestaurantById(RestaurantId restaurantId) {
        return loadRestaurantPort.loadById(restaurantId);
    }
}