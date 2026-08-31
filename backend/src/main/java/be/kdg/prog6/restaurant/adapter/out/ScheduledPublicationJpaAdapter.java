package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.restaurant.domain.ScheduledPublication;
import be.kdg.prog6.restaurant.port.out.LoadScheduledPublicationPort;
import be.kdg.prog6.restaurant.port.out.SaveScheduledPublicationPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class ScheduledPublicationJpaAdapter implements LoadScheduledPublicationPort, SaveScheduledPublicationPort {
    private final ScheduledPublicationJpaRepository repository;
    private final ScheduledPublicationMapper mapper;

    public ScheduledPublicationJpaAdapter(ScheduledPublicationJpaRepository repository,
                                          ScheduledPublicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ScheduledPublication> findPendingPublicationsReadyForExecution(Instant now) {
        return repository.findPendingPublicationsReadyForExecution(now)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional
    @Override
    public ScheduledPublication save(ScheduledPublication scheduledPublication) {
        ScheduledPublicationJpaEntity entity = mapper.toJpaEntity(scheduledPublication);
        repository.save(entity);
        return scheduledPublication;
    }
}
