package be.kdg.prog6.restaurant.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<MenuJpaEntity, UUID> {
    @Query("SELECT m FROM MenuJpaEntity m LEFT JOIN FETCH m.dishes WHERE m.restaurantId = :restaurantId")
    Optional<MenuJpaEntity> findByRestaurantIdWithDishes(UUID restaurantId);
}