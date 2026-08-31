package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.restaurant.domain.Restaurant;
import be.kdg.prog6.restaurant.port.out.SaveRestaurantPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class RestaurantEventPublisher implements SaveRestaurantPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public RestaurantEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        restaurant.getDomainEvents().forEach(applicationEventPublisher::publishEvent);
        return restaurant;
    }
}