package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.order.domain.Basket;
import be.kdg.prog6.order.port.in.RemoveDishFromBasketCommand;
import be.kdg.prog6.order.port.in.RemoveDishFromBasketUseCase;
import be.kdg.prog6.order.port.out.LoadBasketPort;
import be.kdg.prog6.order.port.out.SaveBasketPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RemoveDishFromBasketUseCaseImpl implements RemoveDishFromBasketUseCase {

    private final LoadBasketPort loadBasketPort;
    private final SaveBasketPort saveBasketPort;

    public RemoveDishFromBasketUseCaseImpl(
            LoadBasketPort loadBasketPort,
            SaveBasketPort saveBasketPort) {
        this.loadBasketPort = loadBasketPort;
        this.saveBasketPort = saveBasketPort;
    }

    @Override
    public void removeDish(RemoveDishFromBasketCommand command) {
        CustomerId customerId = CustomerId.of(command.customerId());

        Basket basket = loadBasketPort.loadByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found"));

        DishId dishId = DishId.of(command.dishId());
        basket.removeDish(dishId);

        saveBasketPort.save(basket);
    }
}