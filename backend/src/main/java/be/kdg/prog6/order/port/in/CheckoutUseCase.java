package be.kdg.prog6.order.port.in;

import be.kdg.prog6.common.domain.OrderId;

public interface CheckoutUseCase {
    OrderId checkout(CheckoutCommand command);
}