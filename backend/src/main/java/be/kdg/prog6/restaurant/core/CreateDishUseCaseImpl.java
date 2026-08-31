package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.restaurant.domain.*;
import be.kdg.prog6.restaurant.port.in.CreateDishCommand;
import be.kdg.prog6.restaurant.port.in.CreateDishUseCase;
import be.kdg.prog6.restaurant.port.out.LoadMenuPort;
import be.kdg.prog6.restaurant.port.out.SaveMenuPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreateDishUseCaseImpl implements CreateDishUseCase {

    private final LoadMenuPort loadMenuPort;
    private final List<SaveMenuPort> saveMenuPorts;

    public CreateDishUseCaseImpl(
            LoadMenuPort loadMenuPort,
            List<SaveMenuPort> saveMenuPorts) {
        this.loadMenuPort = loadMenuPort;
        this.saveMenuPorts = saveMenuPorts;
    }

    @Override
    public DishId createDish(CreateDishCommand command) {
        RestaurantId restaurantId = RestaurantId.of(command.restaurantId());

        Menu menu = loadMenuPort.loadByRestaurantId(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found for restaurant"));

        DishName name = new DishName(command.name());
        DishType type = DishType.valueOf(command.type().toUpperCase());
        var foodTags = command.foodTags().stream()
                .map(tag -> FoodTag.valueOf(tag.toUpperCase()))
                .collect(Collectors.toList());
        Description description = new Description(command.description());
        Price price = new Price(command.price());
        ImageUrl imageUrl = new ImageUrl(command.imageUrl());

        DishId dishId = menu.addNewDish(name, type, foodTags, description, price, imageUrl);

        saveMenuPorts.forEach(port -> port.save(menu));
        menu.clearDomainEvents();

        return dishId;
    }
}