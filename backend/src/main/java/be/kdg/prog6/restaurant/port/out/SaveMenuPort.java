package be.kdg.prog6.restaurant.port.out;

import be.kdg.prog6.restaurant.domain.Menu;

public interface SaveMenuPort {
    Menu save(Menu menu);
}