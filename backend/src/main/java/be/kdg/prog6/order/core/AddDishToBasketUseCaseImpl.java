package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.Basket;
import be.kdg.prog6.order.port.in.AddDishToBasketCommand;
import be.kdg.prog6.order.port.in.AddDishToBasketUseCase;
import be.kdg.prog6.order.port.out.LoadBasketPort;
import be.kdg.prog6.order.port.out.SaveBasketPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddDishToBasketUseCaseImpl implements AddDishToBasketUseCase {

    private final LoadBasketPort loadBasketPort;
    private final SaveBasketPort saveBasketPort;

    public AddDishToBasketUseCaseImpl(
            LoadBasketPort loadBasketPort,
            SaveBasketPort saveBasketPort) {
        this.loadBasketPort = loadBasketPort;
        this.saveBasketPort = saveBasketPort;
    }

    @Override
    public void addDish(AddDishToBasketCommand command) {
        CustomerId customerId = CustomerId.of(command.customerId());

        Basket basket = loadBasketPort.loadByCustomerId(customerId)
                .orElseGet(() -> Basket.createNew(customerId));

        RestaurantId restaurantId = RestaurantId.of(command.restaurantId());
        DishId dishId = DishId.of(command.dishId());

        basket.addDish(
                restaurantId,
                dishId,
                command.dishName(),
                command.unitPrice(),
                command.quantity()
        );

        saveBasketPort.save(basket);
    }
}