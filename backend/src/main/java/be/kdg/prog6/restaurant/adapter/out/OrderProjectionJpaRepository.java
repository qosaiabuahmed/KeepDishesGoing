package be.kdg.prog6.restaurant.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderProjectionJpaRepository extends JpaRepository<OrderProjectionJpaEntity, UUID> {
    List<OrderProjectionJpaEntity> findByRestaurantId(UUID restaurantId);
    List<OrderProjectionJpaEntity> findByRestaurantIdAndStatus(UUID restaurantId, String status);
}