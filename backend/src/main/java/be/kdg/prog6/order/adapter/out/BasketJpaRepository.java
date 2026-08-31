package be.kdg.prog6.order.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BasketJpaRepository extends JpaRepository<BasketJpaEntity, UUID> {
    Optional<BasketJpaEntity> findByCustomerId(UUID customerId);
}