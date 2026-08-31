package be.kdg.prog6.restaurant.port.in;

import be.kdg.prog6.common.domain.DishId;

public interface CreateDishUseCase {
    DishId createDish(CreateDishCommand command);
}