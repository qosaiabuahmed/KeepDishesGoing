package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.Restaurant;
import be.kdg.prog6.restaurant.port.in.OpenRestaurantUseCase;
import be.kdg.prog6.restaurant.port.out.LoadRestaurantPort;
import be.kdg.prog6.restaurant.port.out.SaveRestaurantPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OpenRestaurantUseCaseImpl implements OpenRestaurantUseCase {

    private final LoadRestaurantPort loadRestaurantPort;
    private final List<SaveRestaurantPort> saveRestaurantPorts;

    public OpenRestaurantUseCaseImpl(
            LoadRestaurantPort loadRestaurantPort,
            List<SaveRestaurantPort> saveRestaurantPorts) {
        this.loadRestaurantPort = loadRestaurantPort;
        this.saveRestaurantPorts = saveRestaurantPorts;
    }

    @Override
    @Transactional
    public void openRestaurant(RestaurantId restaurantId) {
        Restaurant restaurant = loadRestaurantPort.loadById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        restaurant.openRestaurant();
        saveRestaurantPorts.forEach(port -> port.save(restaurant));
        restaurant.clearDomainEvents();
    }
}