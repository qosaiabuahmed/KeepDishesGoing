package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.port.in.RejectOrderCommand;
import be.kdg.prog6.restaurant.port.in.RejectOrderUseCase;
import be.kdg.prog6.restaurant.port.out.LoadOrderProjectionPort;
import be.kdg.prog6.restaurant.port.out.SaveOrderProjectionPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RejectOrderUseCaseImpl implements RejectOrderUseCase {

    private final LoadOrderProjectionPort loadOrderProjectionPort;
    private final List<SaveOrderProjectionPort> saveOrderProjectionPorts;

    public RejectOrderUseCaseImpl(
            LoadOrderProjectionPort loadOrderProjectionPort,
            List<SaveOrderProjectionPort> saveOrderProjectionPorts) {
        this.loadOrderProjectionPort = loadOrderProjectionPort;
        this.saveOrderProjectionPorts = saveOrderProjectionPorts;
    }

    @Override
    @Transactional
    public void rejectOrder(RejectOrderCommand command) {
        OrderId orderId = new OrderId(UUID.fromString(command.orderId()));
        RestaurantId restaurantId = new RestaurantId(UUID.fromString(command.restaurantId()));

        OrderProjection projection = loadOrderProjectionPort.loadById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + command.orderId()));

        projection.ensureBelongsToRestaurant(restaurantId);

        projection.rejectOrder(command.rejectionReason());

        saveOrderProjectionPorts.forEach(port -> port.save(projection));
    }
}