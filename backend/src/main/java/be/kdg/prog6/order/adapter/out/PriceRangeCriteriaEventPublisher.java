package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.order.domain.PriceRangeCriteria;
import be.kdg.prog6.order.port.out.SavePriceRangeCriteriaPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PriceRangeCriteriaEventPublisher implements SavePriceRangeCriteriaPort {

    private final ApplicationEventPublisher eventPublisher;

    public PriceRangeCriteriaEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void save(PriceRangeCriteria criteria) {
        // Publish all domain events
        criteria.getDomainEvents().forEach(eventPublisher::publishEvent);
        criteria.clearDomainEvents();
    }
}