package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.domain.OrderStatus;
import be.kdg.prog6.order.port.out.LoadOrderPort;
import be.kdg.prog6.order.port.out.SaveOrderPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderJpaAdapter implements LoadOrderPort, SaveOrderPort {

    private final OrderJpaRepository repository;

    public OrderJpaAdapter(OrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Order> loadById(OrderId orderId) {
        return repository.findById(orderId.value())
                .map(OrderMapper::toDomain);
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity jpaEntity = OrderMapper.toJpaEntity(order);
        repository.save(jpaEntity);
        return order;
    }

    @Override
    public List<Order> findByStatusAndOrderTimeBefore(OrderStatus status, LocalDateTime time) {
        return repository.findByStatusAndOrderTimeBefore(status, time).stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }
}