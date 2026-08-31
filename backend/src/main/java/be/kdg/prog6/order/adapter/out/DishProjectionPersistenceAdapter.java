package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.common.domain.StockStatus;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.DishProjection;
import be.kdg.prog6.order.port.out.LoadDishProjectionPort;
import be.kdg.prog6.order.port.out.SaveDishProjectionPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DishProjectionPersistenceAdapter implements LoadDishProjectionPort, SaveDishProjectionPort {

    private final DishProjectionJpaRepository repository;

    public DishProjectionPersistenceAdapter(DishProjectionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DishProjection> loadByRestaurantId(RestaurantId restaurantId) {
        return repository.findByRestaurantId(restaurantId.value()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<DishProjection> loadById(DishId dishId) {
        return repository.findById(dishId.value())
                .map(this::toDomain);
    }

    @Override
    public void save(DishProjection projection) {
        DishProjectionJpaEntity entity = toEntity(projection);
        repository.save(entity);
    }

    @Override
    public void deleteById(DishId dishId) {
        repository.deleteById(dishId.value());
    }

    private DishProjection toDomain(DishProjectionJpaEntity entity) {
        return new DishProjection(
                DishId.of(entity.getDishId().toString()),
                RestaurantId.of(entity.getRestaurantId().toString()),
                entity.getDishName(),
                entity.getDishType(),
                entity.getFoodTags(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImageUrl(),
                mapStockStatus(entity.getStatus())
        );
    }

    private DishProjectionJpaEntity toEntity(DishProjection projection) {
        return new DishProjectionJpaEntity(
                projection.getDishId().value(),
                projection.getRestaurantId().value(),
                projection.getDishName(),
                projection.getDishType(),
                projection.getFoodTags(),
                projection.getDescription(),
                projection.getPrice(),
                projection.getImageUrl(),
                mapStockStatus(projection.getStatus())
        );
    }

    private StockStatus mapStockStatus(DishProjectionJpaEntity.StockStatus jpaStatus) {
        return jpaStatus == DishProjectionJpaEntity.StockStatus.IN_STOCK
                ? StockStatus.IN_STOCK
                : StockStatus.OUT_OF_STOCK;
    }

    private DishProjectionJpaEntity.StockStatus mapStockStatus(StockStatus domainStatus) {
        return domainStatus == StockStatus.IN_STOCK
                ? DishProjectionJpaEntity.StockStatus.IN_STOCK
                : DishProjectionJpaEntity.StockStatus.OUT_OF_STOCK;
    }
}