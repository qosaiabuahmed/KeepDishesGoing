package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.order.domain.Basket;
import be.kdg.prog6.order.port.in.ClearBasketCommand;
import be.kdg.prog6.order.port.in.ClearBasketUseCase;
import be.kdg.prog6.order.port.out.LoadBasketPort;
import be.kdg.prog6.order.port.out.SaveBasketPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClearBasketUseCaseImpl implements ClearBasketUseCase {

    private final LoadBasketPort loadBasketPort;
    private final SaveBasketPort saveBasketPort;

    public ClearBasketUseCaseImpl(
            LoadBasketPort loadBasketPort,
            SaveBasketPort saveBasketPort) {
        this.loadBasketPort = loadBasketPort;
        this.saveBasketPort = saveBasketPort;
    }

    @Override
    public void clearBasket(ClearBasketCommand command) {
        CustomerId customerId = CustomerId.of(command.customerId());

        Basket basket = loadBasketPort.loadByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found"));

        basket.clear();
        saveBasketPort.save(basket);
    }
}