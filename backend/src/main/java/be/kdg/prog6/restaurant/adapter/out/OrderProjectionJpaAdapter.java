package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.domain.OrderProjectionStatus;
import be.kdg.prog6.restaurant.port.out.LoadOrderProjectionPort;
import be.kdg.prog6.restaurant.port.out.SaveOrderProjectionPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderProjectionJpaAdapter implements LoadOrderProjectionPort, SaveOrderProjectionPort {

    private final OrderProjectionJpaRepository repository;

    public OrderProjectionJpaAdapter(OrderProjectionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<OrderProjection> loadById(OrderId orderId) {
        return repository.findById(orderId.value())
                .map(OrderProjectionMapper::toDomain);
    }

    @Override
    public List<OrderProjection> loadByRestaurantId(RestaurantId restaurantId) {
        return repository.findByRestaurantId(restaurantId.value()).stream()
                .map(OrderProjectionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderProjection> loadByRestaurantIdAndStatus(RestaurantId restaurantId, OrderProjectionStatus status) {
        return repository.findByRestaurantIdAndStatus(restaurantId.value(), status.name()).stream()
                .map(OrderProjectionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void save(OrderProjection orderProjection) {
        repository.save(OrderProjectionMapper.toJpaEntity(orderProjection));
    }
}