package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.common.events.PriceRangeCriteriaUpdatedEvent;
import be.kdg.prog6.order.domain.PriceRangeCriteria;
import be.kdg.prog6.order.port.out.LoadPriceRangeCriteriaHistoryPort;
import be.kdg.prog6.order.port.out.LoadPriceRangeCriteriaPort;
import be.kdg.prog6.order.port.out.SavePriceRangeCriteriaPort;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PriceRangeCriteriaJpaAdapter implements LoadPriceRangeCriteriaPort, SavePriceRangeCriteriaPort, LoadPriceRangeCriteriaHistoryPort {

    private final PriceRangeCriteriaEventJpaRepository repository;

    public PriceRangeCriteriaJpaAdapter(PriceRangeCriteriaEventJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<PriceRangeCriteria> loadCurrent() {
        return repository.findCurrentCriteria()
                .map(this::toDomain);
    }

    @Override
    public Optional<PriceRangeCriteria> loadAtDate(LocalDateTime date) {
        return repository.findCriteriaAtDate(date)
                .map(this::toDomain);
    }

    @Override
    public void save(PriceRangeCriteria criteria) {
        PriceRangeCriteriaEventJpaEntity entity = new PriceRangeCriteriaEventJpaEntity(
                UUID.randomUUID(),
                criteria.getLowThreshold(),
                criteria.getMediumThreshold(),
                criteria.getHighThreshold(),
                criteria.getEffectiveDate(),
                java.time.LocalDateTime.now()
        );
        repository.save(entity);
    }

    @Override
    public List<PriceRangeCriteria> loadAllHistory() {
        return repository.findAllByOrderByEffectiveDateAsc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private PriceRangeCriteria toDomain(PriceRangeCriteriaEventJpaEntity entity) {
        return new PriceRangeCriteria(
                entity.getLowThreshold(),
                entity.getMediumThreshold(),
                entity.getHighThreshold(),
                entity.getEffectiveDate()
        );
    }
}