package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.restaurant.domain.OwnerId;
import be.kdg.prog6.restaurant.domain.Restaurant;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.RestaurantName;
import be.kdg.prog6.restaurant.port.out.LoadRestaurantPort;
import be.kdg.prog6.restaurant.port.out.SaveRestaurantPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
public class RestaurantJpaAdapter implements LoadRestaurantPort, SaveRestaurantPort {

    private final RestaurantJpaRepository restaurantRepository;
    private final RestaurantJpaMapper mapper;

    public RestaurantJpaAdapter(
            RestaurantJpaRepository restaurantRepository,
            RestaurantJpaMapper mapper) {
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Restaurant> loadByName(RestaurantName name) {
        return restaurantRepository.findByName(name.value())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<Restaurant> loadById(RestaurantId id) {
        return restaurantRepository.findById(id.value())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<Restaurant> loadByOwnerId(OwnerId ownerId) {
        return restaurantRepository.findByOwnerId(ownerId.value())
                .map(mapper::toDomainModel);
    }

    @Override
    public List<Restaurant> loadAll() {
        return restaurantRepository.findAll().stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantJpaEntity entity = mapper.toJpaEntity(restaurant);
        restaurantRepository.save(entity);
        return restaurant;
    }
}