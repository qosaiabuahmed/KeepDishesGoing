package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.port.out.SaveOrderPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher implements SaveOrderPort {

    private final ApplicationEventPublisher eventPublisher;

    public OrderEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Order save(Order order) {
        order.getDomainEvents().forEach(eventPublisher::publishEvent);
        order.clearDomainEvents();
        return order;
    }
}