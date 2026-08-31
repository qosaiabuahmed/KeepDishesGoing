package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.order.domain.*;
import be.kdg.prog6.order.port.in.CheckoutCommand;
import be.kdg.prog6.order.port.in.CheckoutUseCase;
import be.kdg.prog6.order.port.in.ValidateBasketQuery;
import be.kdg.prog6.order.port.out.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutUseCaseImpl implements CheckoutUseCase {

    private final ValidateBasketQuery validateBasketQuery;
    private final LoadBasketPort loadBasketPort;
    private final SaveBasketPort saveBasketPort;
    private final PaymentPort paymentPort;
    private final List<SaveOrderPort> saveOrderPorts;
    private final LoadRestaurantProjectionPort loadRestaurantProjectionPort;

    public CheckoutUseCaseImpl(
            ValidateBasketQuery validateBasketQuery,
            LoadBasketPort loadBasketPort,
            SaveBasketPort saveBasketPort,
            PaymentPort paymentPort,
            List<SaveOrderPort> saveOrderPorts,
            LoadRestaurantProjectionPort loadRestaurantProjectionPort) {
        this.validateBasketQuery = validateBasketQuery;
        this.loadBasketPort = loadBasketPort;
        this.saveBasketPort = saveBasketPort;
        this.paymentPort = paymentPort;
        this.saveOrderPorts = saveOrderPorts;
        this.loadRestaurantProjectionPort = loadRestaurantProjectionPort;
    }

    @Override
    public OrderId checkout(CheckoutCommand command) {
        BasketValidationResult validationResult = validateBasketQuery.validateBasket(command.customerId());
        if (!validationResult.isValid()) {
            throw new IllegalStateException("Cannot checkout: basket has validation issues");
        }

        CustomerId customerId = CustomerId.of(command.customerId());
        Basket basket = loadBasketPort.loadByCustomerId(customerId)
                .orElseThrow(() -> new IllegalStateException("Basket not found"));

        RestaurantProjection restaurant = loadRestaurantProjectionPort.loadById(basket.getRestaurantId())
                .orElseThrow(() -> new IllegalStateException("Restaurant not found"));

        basket.validateForCheckout(restaurant);

        PaymentResult paymentResult = paymentPort.processPayment(
                command.customerId(),
                basket.calculateTotal(),
                command.currency()
        );

        if (!paymentResult.success()) {
            throw new IllegalStateException("Payment failed: " + paymentResult.message());
        }

        DeliveryAddress deliveryAddress = new DeliveryAddress(
                command.street(),
                command.number(),
                command.city(),
                command.postalCode(),
                command.country()
        );

        List<OrderItem> orderItems = basket.getItems().stream()
                .map(item -> new OrderItem(
                        item.getDishId(),
                        item.getDishName(),
                        item.getUnitPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        OrderId orderId = OrderId.newId();
        Order order = new Order(
                orderId,
                customerId,
                basket.getRestaurantId(),
                orderItems,
                deliveryAddress,
                command.customerName(),
                command.contactEmail(),
                paymentResult.transactionId()
        );

        saveOrderPorts.forEach(port -> port.save(order));

        basket.clear();
        saveBasketPort.save(basket);

        return orderId;
    }
}