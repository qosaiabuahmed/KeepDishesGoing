package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.events.external.AddressDto;
import be.kdg.prog6.common.events.external.Coordinates;
import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.domain.Restaurant;
import be.kdg.prog6.restaurant.port.in.AcceptOrderCommand;
import be.kdg.prog6.restaurant.port.in.AcceptOrderUseCase;
import be.kdg.prog6.restaurant.port.out.LoadOrderProjectionPort;
import be.kdg.prog6.restaurant.port.out.LoadRestaurantPort;
import be.kdg.prog6.restaurant.port.out.SaveOrderProjectionPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AcceptOrderUseCaseImpl implements AcceptOrderUseCase {

    private final LoadOrderProjectionPort loadOrderProjectionPort;
    private final LoadRestaurantPort loadRestaurantPort;
    private final List<SaveOrderProjectionPort> saveOrderProjectionPorts;

    public AcceptOrderUseCaseImpl(
            LoadOrderProjectionPort loadOrderProjectionPort,
            LoadRestaurantPort loadRestaurantPort,
            List<SaveOrderProjectionPort> saveOrderProjectionPorts) {
        this.loadOrderProjectionPort = loadOrderProjectionPort;
        this.loadRestaurantPort = loadRestaurantPort;
        this.saveOrderProjectionPorts = saveOrderProjectionPorts;
    }

    @Override
    @Transactional
    public void acceptOrder(AcceptOrderCommand command) {
        OrderId orderId = new OrderId(UUID.fromString(command.orderId()));
        RestaurantId restaurantId = new RestaurantId(UUID.fromString(command.restaurantId()));

        OrderProjection projection = loadOrderProjectionPort.loadById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + command.orderId()));

        projection.ensureBelongsToRestaurant(restaurantId);

        Restaurant restaurant = loadRestaurantPort.loadById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + command.restaurantId()));

        AddressDto pickupAddress = new AddressDto(
                restaurant.getAddress().street(),
                restaurant.getAddress().number(),
                restaurant.getAddress().postalCode(),
                restaurant.getAddress().city()
        );

        AddressDto dropoffAddress = new AddressDto(
                projection.getDeliveryStreet(),
                projection.getDeliveryNumber(),
                projection.getDeliveryPostalCode(),
                projection.getDeliveryCity()
        );


        Coordinates pickupCoordinates = new Coordinates(50.8467, 4.3547);
        Coordinates dropoffCoordinates = new Coordinates(50.8325, 4.3808);

        projection.acceptOrder(pickupAddress, pickupCoordinates, dropoffAddress, dropoffCoordinates);

        saveOrderProjectionPorts.forEach(port -> port.save(projection));
    }
}