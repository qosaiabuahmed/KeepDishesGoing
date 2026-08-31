package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.*;
import java.util.stream.Collectors;

public class MenuMapper {
    public static MenuJpaEntity toJpaEntity(Menu menu) {
        MenuJpaEntity jpa = new MenuJpaEntity(menu.getId().value(), menu.getRestaurantId().value());
        var dishes = menu.getDishes().stream()
                .map(dish -> DishMapper.toJpaEntity(dish, jpa))
                .collect(Collectors.toList());
        jpa.setDishes(dishes);
        return jpa;
    }

    public static Menu toDomain(MenuJpaEntity jpa) {
        MenuId menuId = new MenuId(jpa.getId());
        RestaurantId restaurantId = new RestaurantId(jpa.getRestaurantId());
        var dishes = jpa.getDishes().stream()
                .map(DishMapper::toDomain)
                .collect(Collectors.toList());
        return new Menu(menuId, restaurantId, dishes);
    }
}