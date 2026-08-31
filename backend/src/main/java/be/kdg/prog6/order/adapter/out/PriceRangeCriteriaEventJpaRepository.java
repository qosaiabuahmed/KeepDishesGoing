package be.kdg.prog6.order.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PriceRangeCriteriaEventJpaRepository extends JpaRepository<PriceRangeCriteriaEventJpaEntity, UUID> {

    @Query("SELECT e FROM PriceRangeCriteriaEventJpaEntity e ORDER BY e.effectiveDate DESC LIMIT 1")
    Optional<PriceRangeCriteriaEventJpaEntity> findCurrentCriteria();

    @Query("SELECT e FROM PriceRangeCriteriaEventJpaEntity e WHERE e.effectiveDate <= :date ORDER BY e.effectiveDate DESC LIMIT 1")
    Optional<PriceRangeCriteriaEventJpaEntity> findCriteriaAtDate(@Param("date") LocalDateTime date);

    List<PriceRangeCriteriaEventJpaEntity> findAllByOrderByEffectiveDateAsc();
}