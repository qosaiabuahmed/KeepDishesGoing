package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.port.in.MarkOrderReadyCommand;
import be.kdg.prog6.restaurant.port.in.MarkOrderReadyUseCase;
import be.kdg.prog6.restaurant.port.out.LoadOrderProjectionPort;
import be.kdg.prog6.restaurant.port.out.SaveOrderProjectionPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class MarkOrderReadyUseCaseImpl implements MarkOrderReadyUseCase {

    private final LoadOrderProjectionPort loadOrderProjectionPort;
    private final List<SaveOrderProjectionPort> saveOrderProjectionPorts;
    private static final Logger log = LoggerFactory.getLogger(MarkOrderReadyUseCaseImpl.class);

    public MarkOrderReadyUseCaseImpl(
            LoadOrderProjectionPort loadOrderProjectionPort,
            List<SaveOrderProjectionPort> saveOrderProjectionPorts) {
        this.loadOrderProjectionPort = loadOrderProjectionPort;
        this.saveOrderProjectionPorts = saveOrderProjectionPorts;
    }

    @Override
    @Transactional
    public void markOrderReady(MarkOrderReadyCommand command) {
        OrderId orderId = new OrderId(UUID.fromString(command.orderId()));
        RestaurantId restaurantId = new RestaurantId(UUID.fromString(command.restaurantId()));

        OrderProjection projection = loadOrderProjectionPort.loadById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + command.orderId()));

        projection.ensureBelongsToRestaurant(restaurantId);

        projection.markOrderReady();


        log.info("Order before save: status={}, domainEvents={}",
                projection.getStatus(),
                projection.getDomainEvents().stream()
                        .map(e -> e.getClass().getSimpleName()).toList());

        saveOrderProjectionPorts.forEach(port -> {
            log.info("Invoking SaveOrderProjectionPort: {}", port.getClass().getName());
            port.save(projection);
        });

    }
}