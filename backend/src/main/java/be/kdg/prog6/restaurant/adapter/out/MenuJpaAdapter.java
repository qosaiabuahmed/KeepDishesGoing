package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.*;
import be.kdg.prog6.restaurant.port.out.*;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class MenuJpaAdapter implements LoadMenuPort, SaveMenuPort {
    private final MenuJpaRepository menuJpaRepository;

    public MenuJpaAdapter(MenuJpaRepository menuJpaRepository) {
        this.menuJpaRepository = menuJpaRepository;
    }

    @Override
    public Optional<Menu> loadById(MenuId menuId) {
        return menuJpaRepository.findById(menuId.value()).map(MenuMapper::toDomain);
    }

    @Override
    public Optional<Menu> loadByRestaurantId(RestaurantId restaurantId) {
        return menuJpaRepository.findByRestaurantIdWithDishes(restaurantId.value()).map(MenuMapper::toDomain);
    }

    @Override
    public Menu save(Menu menu) {
        menuJpaRepository.save(MenuMapper.toJpaEntity(menu));
        return menu;
    }
}