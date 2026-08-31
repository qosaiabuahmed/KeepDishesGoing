package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.port.out.SaveOrderProjectionPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderProjectionEventPublisher implements SaveOrderProjectionPort {

    private final ApplicationEventPublisher eventPublisher;

    public OrderProjectionEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void save(OrderProjection orderProjection) {
        orderProjection.getDomainEvents().forEach(eventPublisher::publishEvent);
        orderProjection.clearDomainEvents();
    }
}