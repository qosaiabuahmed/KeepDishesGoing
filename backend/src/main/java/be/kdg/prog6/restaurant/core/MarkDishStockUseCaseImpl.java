package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.restaurant.domain.Menu;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.port.in.MarkDishInStockCommand;
import be.kdg.prog6.restaurant.port.in.MarkDishOutOfStockCommand;
import be.kdg.prog6.restaurant.port.in.MarkDishStockUseCase;
import be.kdg.prog6.restaurant.port.out.LoadMenuPort;
import be.kdg.prog6.restaurant.port.out.SaveMenuPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MarkDishStockUseCaseImpl implements MarkDishStockUseCase {

    private final LoadMenuPort loadMenuPort;
    private final List<SaveMenuPort> saveMenuPorts;

    public MarkDishStockUseCaseImpl(
            LoadMenuPort loadMenuPort,
            List<SaveMenuPort> saveMenuPorts) {
        this.loadMenuPort = loadMenuPort;
        this.saveMenuPorts = saveMenuPorts;
    }

    @Override
    public void markOutOfStock(MarkDishOutOfStockCommand command) {
        RestaurantId restaurantId = RestaurantId.of(command.restaurantId());
        Menu menu = loadMenuPort.loadByRestaurantId(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        DishId dishId = DishId.of(command.dishId());
        menu.markDishOutOfStock(dishId);

        saveMenuPorts.forEach(port -> port.save(menu));
        menu.clearDomainEvents();
    }

    @Override
    public void markInStock(MarkDishInStockCommand command) {
        RestaurantId restaurantId = RestaurantId.of(command.restaurantId());
        Menu menu = loadMenuPort.loadByRestaurantId(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        DishId dishId = DishId.of(command.dishId());
        menu.markDishInStock(dishId);

        saveMenuPorts.forEach(port -> port.save(menu));
        menu.clearDomainEvents();
    }
}