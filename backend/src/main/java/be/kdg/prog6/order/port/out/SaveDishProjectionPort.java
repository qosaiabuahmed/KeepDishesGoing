package be.kdg.prog6.order.port.out;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.order.domain.DishProjection;

public interface SaveDishProjectionPort {
    void save(DishProjection projection);
    void deleteById(DishId dishId);
}