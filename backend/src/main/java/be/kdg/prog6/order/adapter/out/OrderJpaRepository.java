package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    List<OrderJpaEntity> findByStatusAndOrderTimeBefore(OrderStatus status, LocalDateTime time);
}