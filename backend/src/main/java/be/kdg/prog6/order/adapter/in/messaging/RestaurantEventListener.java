package be.kdg.prog6.order.adapter.in.messaging;

import be.kdg.prog6.common.domain.StockStatus;
import be.kdg.prog6.common.events.*;
import be.kdg.prog6.order.domain.DishProjection;
import be.kdg.prog6.order.domain.PriceRangeCriteria;
import be.kdg.prog6.order.domain.RestaurantProjection;
import be.kdg.prog6.order.port.out.LoadDishProjectionPort;
import be.kdg.prog6.order.port.out.SaveDishProjectionPort;
import be.kdg.prog6.order.port.out.LoadRestaurantProjectionPort;
import be.kdg.prog6.order.port.out.SaveRestaurantProjectionPort;
import be.kdg.prog6.order.port.out.LoadOrderPort;
import be.kdg.prog6.order.port.out.SaveOrderPort;
import be.kdg.prog6.order.port.out.LoadPriceRangeCriteriaPort;
import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.domain.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class RestaurantEventListener {

    private static final Logger log = LoggerFactory.getLogger(RestaurantEventListener.class);

    private final SaveRestaurantProjectionPort saveRestaurantProjectionPort;
    private final LoadRestaurantProjectionPort loadRestaurantProjectionPort;
    private final SaveDishProjectionPort saveDishProjectionPort;
    private final LoadDishProjectionPort loadDishProjectionPort;
    private final LoadOrderPort loadOrderPort;
    private final List<SaveOrderPort> saveOrderPort;
    private final LoadPriceRangeCriteriaPort loadPriceRangeCriteriaPort;

    public RestaurantEventListener(
            SaveRestaurantProjectionPort saveRestaurantProjectionPort,
            LoadRestaurantProjectionPort loadRestaurantProjectionPort,
            SaveDishProjectionPort saveDishProjectionPort,
            LoadDishProjectionPort loadDishProjectionPort,
            LoadOrderPort loadOrderPort,
            List<SaveOrderPort> saveOrderPort,
            LoadPriceRangeCriteriaPort loadPriceRangeCriteriaPort) {
        this.saveRestaurantProjectionPort = saveRestaurantProjectionPort;
        this.loadRestaurantProjectionPort = loadRestaurantProjectionPort;
        this.saveDishProjectionPort = saveDishProjectionPort;
        this.loadDishProjectionPort = loadDishProjectionPort;
        this.loadOrderPort = loadOrderPort;
        this.saveOrderPort = saveOrderPort;
        this.loadPriceRangeCriteriaPort = loadPriceRangeCriteriaPort;
    }

    @EventListener
    @Transactional
    public void on(DishPublishedEvent event) {
        log.info("Received DishPublishedEvent for dish: {}", event.dishId());

        DishProjection projection = new DishProjection(
                event.dishId(),
                event.restaurantId(),
                event.dishName(),
                event.dishType(),
                event.foodTags(),
                event.description(),
                event.price(),
                event.imageUrl(),
                StockStatus.IN_STOCK
        );

        saveDishProjectionPort.save(projection);
        log.info("Created DishProjection for dish: {}", event.dishId());

        recalculateRestaurantPriceRange(event.restaurantId());
    }

    @EventListener
    @Transactional
    public void on(DishUnpublishedEvent event) {
        log.info("Received DishUnpublishedEvent for dish: {}", event.dishId());

        saveDishProjectionPort.deleteById(event.dishId());
        log.info("Deleted DishProjection for dish: {}", event.dishId());

        recalculateRestaurantPriceRange(event.restaurantId());
    }

    @EventListener
    @Transactional
    public void on(DishMarkedOutOfStockEvent event) {
        log.info("Received DishMarkedOutOfStockEvent for dish: {}", event.dishId());

        loadDishProjectionPort.loadById(event.dishId()).ifPresent(projection -> {
            projection.markOutOfStock();
            saveDishProjectionPort.save(projection);
            log.info("Updated DishProjection status to OUT_OF_STOCK for dish: {}", event.dishId());
        });
    }

    @EventListener
    @Transactional
    public void on(DishMarkedInStockEvent event) {
        log.info("Received DishMarkedInStockEvent for dish: {}", event.dishId());

        loadDishProjectionPort.loadById(event.dishId()).ifPresent(projection -> {
            projection.markInStock();
            saveDishProjectionPort.save(projection);
            log.info("Updated DishProjection status to IN_STOCK for dish: {}", event.dishId());
        });
    }

    @EventListener
    @Transactional
    public void on(RestaurantCreatedEvent event) {
        log.info("Received RestaurantCreatedEvent for restaurant: {}", event.restaurantId());

        RestaurantProjection projection = new RestaurantProjection(
                event.restaurantId(),
                event.name(),
                event.cuisine(),
                event.street(),
                event.number(),
                event.postalCode(),
                event.city(),
                event.country(),
                event.contactEmail(),
                event.imageUrls(),
                event.isOpen(),
                "CHEAP",
                java.math.BigDecimal.ZERO
        );

        saveRestaurantProjectionPort.save(projection);
        log.info("Created RestaurantProjection for restaurant: {}", event.restaurantId());
    }

    @EventListener
    @Transactional
    public void on(RestaurantOpenedEvent event) {
        log.info("Received RestaurantOpenedEvent for restaurant: {}", event.restaurantId());

        loadRestaurantProjectionPort.loadById(event.restaurantId()).ifPresent(projection -> {
            projection.setOpen(true);
            saveRestaurantProjectionPort.save(projection);
            log.info("Updated RestaurantProjection status to OPEN for restaurant: {}", event.restaurantId());
        });
    }

    @EventListener
    @Transactional
    public void on(RestaurantClosedEvent event) {
        log.info("Received RestaurantClosedEvent for restaurant: {}", event.restaurantId());

        loadRestaurantProjectionPort.loadById(event.restaurantId()).ifPresent(projection -> {
            projection.setOpen(false);
            saveRestaurantProjectionPort.save(projection);
            log.info("Updated RestaurantProjection status to CLOSED for restaurant: {}", event.restaurantId());
        });
    }

    @EventListener
    @Transactional
    public void on(OrderAcceptedEvent event) {
        log.info("Received OrderAcceptedEvent for order: {}", event.orderId());

        loadOrderPort.loadById(new be.kdg.prog6.common.domain.OrderId(event.orderId())).ifPresent(order -> {
            order.updateStatus(OrderStatus.CONFIRMED);
            saveOrderPort.forEach(port -> port.save(order));
            log.info("Updated Order status to CONFIRMED for order: {}", event.orderId());
        });
    }

    @EventListener
    @Transactional
    public void on(OrderRejectedEvent event) {
        log.info("Received OrderRejectedEvent for order: {}", event.orderId());

        loadOrderPort.loadById(event.orderId()).ifPresent(order -> {
            order.updateStatus(OrderStatus.CANCELLED);
            saveOrderPort.forEach(port -> port.save(order));
            log.info("Updated Order status to CANCELLED for order: {}", event.orderId());
        });
    }

    @EventListener
    @Transactional
    public void on(OrderReadyForPickupEvent event) {
        log.info("Received OrderReadyEvent for order: {}", event.orderId());

        loadOrderPort.loadById(new be.kdg.prog6.common.domain.OrderId(event.orderId())).ifPresent(order -> {
            order.updateStatus(OrderStatus.READY);
            saveOrderPort.forEach(port -> port.save(order));
            log.info("Updated Order status to READY for order: {}", event.orderId());
        });
    }

    private void recalculateRestaurantPriceRange(be.kdg.prog6.common.domain.RestaurantId restaurantId) {
        loadRestaurantProjectionPort.loadById(restaurantId).ifPresent(restaurantProjection -> {
            List<DishProjection> dishes = loadDishProjectionPort.loadByRestaurantId(restaurantId);

            if (dishes.isEmpty()) {
                restaurantProjection.setPriceRange("CHEAP");
                restaurantProjection.setAverageMenuPrice(java.math.BigDecimal.ZERO);
            } else {
                java.math.BigDecimal totalPrice = dishes.stream()
                        .map(DishProjection::getPrice)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

                java.math.BigDecimal avgPrice = totalPrice.divide(
                        java.math.BigDecimal.valueOf(dishes.size()),
                        2,
                        java.math.RoundingMode.HALF_UP
                );

                String priceRange = calculatePriceRange(avgPrice);

                restaurantProjection.setPriceRange(priceRange);
                restaurantProjection.setAverageMenuPrice(avgPrice);
            }

            saveRestaurantProjectionPort.save(restaurantProjection);
            log.info("Recalculated price range for restaurant: {} - Range: {}, Avg: {}",
                    restaurantId, restaurantProjection.getPriceRange(), restaurantProjection.getAverageMenuPrice());
        });
    }

    private String calculatePriceRange(java.math.BigDecimal avgPrice) {
        PriceRangeCriteria criteria = loadPriceRangeCriteriaPort.loadCurrent()
                .orElseGet(() -> {
                    log.warn("No criteria found in database, using defaults!");
                    return new PriceRangeCriteria(
                            new java.math.BigDecimal("10"),
                            new java.math.BigDecimal("30"),
                            new java.math.BigDecimal("60"),
                            java.time.LocalDateTime.now()
                    );
                });

        log.info("Using criteria: low={}, medium={}, high={} for avgPrice={}",
                criteria.getLowThreshold(), criteria.getMediumThreshold(),
                criteria.getHighThreshold(), avgPrice);

        String result = criteria.calculatePriceRange(avgPrice);
        log.info("Result: {}", result);
        return result;
    }

    @EventListener
    @Transactional
    public void on(PriceRangeCriteriaUpdatedEvent event) {
        log.info("Received PriceRangeCriteriaUpdatedEvent - recalculating all restaurant price ranges");

        loadRestaurantProjectionPort.loadAll().forEach(restaurantProjection -> {
            recalculateRestaurantPriceRange(restaurantProjection.getRestaurantId());
        });

        log.info("Finished recalculating all restaurant price ranges with new criteria");
    }
}