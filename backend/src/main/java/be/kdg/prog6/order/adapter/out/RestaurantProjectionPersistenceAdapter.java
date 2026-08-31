package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.RestaurantProjection;
import be.kdg.prog6.order.port.out.LoadRestaurantProjectionPort;
import be.kdg.prog6.order.port.out.SaveRestaurantProjectionPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantProjectionPersistenceAdapter implements LoadRestaurantProjectionPort, SaveRestaurantProjectionPort {

    private final RestaurantProjectionJpaRepository repository;

    public RestaurantProjectionPersistenceAdapter(RestaurantProjectionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RestaurantProjection> loadAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<RestaurantProjection> loadById(RestaurantId restaurantId) {
        return repository.findById(restaurantId.value())
                .map(this::toDomain);
    }

    @Override
    public void save(RestaurantProjection projection) {
        RestaurantProjectionJpaEntity entity = toEntity(projection);
        repository.save(entity);
    }

    private RestaurantProjection toDomain(RestaurantProjectionJpaEntity entity) {
        return new RestaurantProjection(
                RestaurantId.of(entity.getRestaurantId().toString()),
                entity.getName(),
                entity.getCuisine(),
                entity.getStreet(),
                entity.getNumber(),
                entity.getPostalCode(),
                entity.getCity(),
                entity.getCountry(),
                entity.getContactEmail(),
                entity.getImageUrls(),
                entity.isOpen(),
                entity.getPriceRange(),
                entity.getAverageMenuPrice()
        );
    }

    private RestaurantProjectionJpaEntity toEntity(RestaurantProjection projection) {
        return new RestaurantProjectionJpaEntity(
               projection.getRestaurantId().value(),
                projection.getName(),
                projection.getCuisine(),
                projection.getStreet(),
                projection.getNumber(),
                projection.getPostalCode(),
                projection.getCity(),
                projection.getCountry(),
                projection.getContactEmail(),
                projection.getImageUrls(),
                projection.isOpen(),
                projection.getPriceRange(),
                projection.getAverageMenuPrice()
        );
    }
}