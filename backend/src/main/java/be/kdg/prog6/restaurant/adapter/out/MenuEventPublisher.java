package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.restaurant.domain.Menu;
import be.kdg.prog6.restaurant.port.out.SaveMenuPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MenuEventPublisher implements SaveMenuPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public MenuEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Menu save(Menu menu) {
        menu.getDomainEvents().forEach(applicationEventPublisher::publishEvent);
        return menu;
    }
}