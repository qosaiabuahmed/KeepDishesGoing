package be.kdg.prog6.restaurant.adapter.in;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.adapter.in.dto.OrderProjectionDto;
import be.kdg.prog6.restaurant.adapter.in.request.RejectOrderRequest;
import be.kdg.prog6.restaurant.adapter.in.security.CurrentUserProvider;
import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.port.in.AcceptOrderCommand;
import be.kdg.prog6.restaurant.port.in.AcceptOrderUseCase;
import be.kdg.prog6.restaurant.port.in.MarkOrderReadyCommand;
import be.kdg.prog6.restaurant.port.in.MarkOrderReadyUseCase;
import be.kdg.prog6.restaurant.port.in.RejectOrderCommand;
import be.kdg.prog6.restaurant.port.in.RejectOrderUseCase;
import be.kdg.prog6.restaurant.port.out.LoadOrderProjectionPort;
import be.kdg.prog6.restaurant.port.out.LoadRestaurantPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/orders")
public class OrderManagementController {

    private final AcceptOrderUseCase acceptOrderUseCase;
    private final RejectOrderUseCase rejectOrderUseCase;
    private final MarkOrderReadyUseCase markOrderReadyUseCase;
    private final CurrentUserProvider currentUserProvider;
    private final LoadRestaurantPort loadRestaurantPort;
    private final LoadOrderProjectionPort loadOrderProjectionPort;

    public OrderManagementController(
            AcceptOrderUseCase acceptOrderUseCase,
            RejectOrderUseCase rejectOrderUseCase,
            MarkOrderReadyUseCase markOrderReadyUseCase,
            CurrentUserProvider currentUserProvider,
            LoadRestaurantPort loadRestaurantPort,
            LoadOrderProjectionPort loadOrderProjectionPort) {
        this.acceptOrderUseCase = acceptOrderUseCase;
        this.rejectOrderUseCase = rejectOrderUseCase;
        this.markOrderReadyUseCase = markOrderReadyUseCase;
        this.currentUserProvider = currentUserProvider;
        this.loadRestaurantPort = loadRestaurantPort;
        this.loadOrderProjectionPort = loadOrderProjectionPort;
    }

    @GetMapping
    public ResponseEntity<List<OrderProjectionDto>> getOrders(@PathVariable String restaurantId) {
        String ownerId = currentUserProvider.getCurrentUserId();
        verifyOwnership(restaurantId, ownerId);

        List<OrderProjection> orders = loadOrderProjectionPort.loadByRestaurantId(
                RestaurantId.of(restaurantId)
        );

        List<OrderProjectionDto> dtos = orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/submitted")
    public ResponseEntity<List<OrderProjectionDto>> getSubmittedOrders(@PathVariable String restaurantId) {
        String ownerId = currentUserProvider.getCurrentUserId();
        verifyOwnership(restaurantId, ownerId);

        List<OrderProjection> orders = loadOrderProjectionPort.loadByRestaurantIdAndStatus(
                RestaurantId.of(restaurantId),
                be.kdg.prog6.restaurant.domain.OrderProjectionStatus.SUBMITTED
        );

        List<OrderProjectionDto> dtos = orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/confirmed")
    public ResponseEntity<List<OrderProjectionDto>> getConfirmedOrders(@PathVariable String restaurantId) {
        String ownerId = currentUserProvider.getCurrentUserId();
        verifyOwnership(restaurantId, ownerId);

        List<OrderProjection> orders = loadOrderProjectionPort.loadByRestaurantIdAndStatus(
                RestaurantId.of(restaurantId),
                be.kdg.prog6.restaurant.domain.OrderProjectionStatus.CONFIRMED
        );

        List<OrderProjectionDto> dtos = orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable String restaurantId,
            @PathVariable String orderId) {

        String ownerId = currentUserProvider.getCurrentUserId();
        verifyOwnership(restaurantId, ownerId);

        AcceptOrderCommand command = new AcceptOrderCommand(orderId, restaurantId);
        acceptOrderUseCase.acceptOrder(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(
            @PathVariable String restaurantId,
            @PathVariable String orderId,
            @RequestBody RejectOrderRequest request) {

        String ownerId = currentUserProvider.getCurrentUserId();
        verifyOwnership(restaurantId, ownerId);

        RejectOrderCommand command = new RejectOrderCommand(orderId, restaurantId, request.reason());
        rejectOrderUseCase.rejectOrder(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/ready")
    public ResponseEntity<Void> markOrderReady(
            @PathVariable String restaurantId,
            @PathVariable String orderId) {

        String ownerId = currentUserProvider.getCurrentUserId();
        verifyOwnership(restaurantId, ownerId);

        MarkOrderReadyCommand command = new MarkOrderReadyCommand(orderId, restaurantId);
        markOrderReadyUseCase.markOrderReady(command);

        return ResponseEntity.ok().build();
    }

    private void verifyOwnership(String restaurantId, String ownerId) {
        var restaurant = loadRestaurantPort.loadById(be.kdg.prog6.common.domain.RestaurantId.of(restaurantId))
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        if (!restaurant.getOwnerId().value().toString().equals(ownerId)) {
            throw new IllegalArgumentException("You do not own this restaurant");
        }
    }

    private OrderProjectionDto toDto(OrderProjection order) {
        var deliveryAddressDto = new OrderProjectionDto.DeliveryAddressDto(
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getDeliveryPostalCode(),
                order.getDeliveryCountry()
        );

        var itemDtos = order.getItems().stream()
                .map(item -> new OrderProjectionDto.OrderItemDto(
                        item.getDishId(),
                        item.getDishName(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getTotalPrice()
                ))
                .collect(Collectors.toList());

        return new OrderProjectionDto(
                order.getOrderId().value().toString(),
                order.getRestaurantId().value().toString(),
                order.getCustomerId().value().toString(),
                order.getCustomerName(),
                order.getContactEmail(),
                deliveryAddressDto,
                itemDtos,
                order.getTotalAmount(),
                order.getPaymentTransactionId(),
                order.getOrderTime(),
                order.getStatus().name(),
                order.getRejectionReason(),
                order.getDecisionTime()
        );
    }
}