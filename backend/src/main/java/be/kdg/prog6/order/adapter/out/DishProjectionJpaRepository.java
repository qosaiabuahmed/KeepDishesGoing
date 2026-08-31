package be.kdg.prog6.order.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishProjectionJpaRepository extends JpaRepository<DishProjectionJpaEntity, UUID> {
    List<DishProjectionJpaEntity> findByRestaurantId(UUID restaurantId);
}