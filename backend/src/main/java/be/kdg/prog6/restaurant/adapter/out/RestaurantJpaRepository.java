package be.kdg.prog6.restaurant.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, UUID> {

    Optional<RestaurantJpaEntity> findByName(String name);

    Optional<RestaurantJpaEntity> findByOwnerId(UUID ownerId);
}