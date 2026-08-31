package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.restaurant.domain.Restaurant;
import be.kdg.prog6.restaurant.port.in.GetAllRestaurantsUseCase;
import be.kdg.prog6.restaurant.port.out.LoadRestaurantPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetAllRestaurantsUseCaseImpl implements GetAllRestaurantsUseCase {

    private final LoadRestaurantPort loadRestaurantPort;

    public GetAllRestaurantsUseCaseImpl(LoadRestaurantPort loadRestaurantPort) {
        this.loadRestaurantPort = loadRestaurantPort;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return loadRestaurantPort.loadAll();
    }
}