package be.kdg.prog6.restaurant.port.out;

import be.kdg.prog6.restaurant.domain.Restaurant;

public interface SaveRestaurantPort {
    Restaurant save(Restaurant restaurant);
}