package be.kdg.prog6.restaurant.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduledPublicationJpaRepository extends JpaRepository<ScheduledPublicationJpaEntity, UUID> {

    @Query("""
        select sp
        from ScheduledPublicationJpaEntity sp
        where sp.status = 'PENDING' and sp.scheduledTime <= :now
    """)
    List<ScheduledPublicationJpaEntity> findPendingPublicationsReadyForExecution(@Param("now") Instant now);

    List<ScheduledPublicationJpaEntity> findByRestaurantIdAndStatus(UUID restaurantId, String status);
}

