package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.restaurant.domain.OwnerId;
import be.kdg.prog6.restaurant.domain.Restaurant;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.RestaurantName;
import be.kdg.prog6.restaurant.port.out.LoadRestaurantPort;
import be.kdg.prog6.restaurant.port.out.SaveRestaurantPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RestaurantInMemoryAdapter implements LoadRestaurantPort, SaveRestaurantPort {

    private static final Logger log = LoggerFactory.getLogger(RestaurantInMemoryAdapter.class);

    private final Map<RestaurantId, Restaurant> restaurantsById = new HashMap<>();
    private final Map<String, Restaurant> restaurantsByName = new HashMap<>();
    private final Map<OwnerId, Restaurant> restaurantsByOwner = new HashMap<>();

    @Override
    public Optional<Restaurant> loadByName(RestaurantName name) {
        return Optional.ofNullable(this.restaurantsByName.get(name.value()));
    }

    @Override
    public Optional<Restaurant> loadById(RestaurantId id) {
        return Optional.ofNullable(this.restaurantsById.get(id));
    }

    @Override
    public Optional<Restaurant> loadByOwnerId(OwnerId ownerId) {
        return Optional.ofNullable(this.restaurantsByOwner.get(ownerId));
    }

    @Override
    public List<Restaurant> loadAll() {
        return new ArrayList<>(this.restaurantsById.values());
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        this.restaurantsById.put(restaurant.getId(), restaurant);
        this.restaurantsByName.put(restaurant.getName().value(), restaurant);
        this.restaurantsByOwner.put(restaurant.getOwnerId(), restaurant);

        log.info("Restaurant saved: {} with ID: {} for Owner: {}",
                restaurant.getName().value(),
                restaurant.getId().value(),
                restaurant.getOwnerId().value());

        return restaurant;
    }
}