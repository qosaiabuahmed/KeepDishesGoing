package be.kdg.prog6.order.adapter.in.web;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.order.adapter.in.dto.*;
import be.kdg.prog6.order.adapter.in.request.CheckoutRequest;
import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.domain.OrderItem;
import be.kdg.prog6.order.port.in.CheckoutCommand;
import be.kdg.prog6.order.port.in.CheckoutUseCase;
import be.kdg.prog6.order.port.in.GetOrderQuery;
import be.kdg.prog6.order.port.in.GetOrderQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
public class CustomerOrderController {

    private final CheckoutUseCase checkoutUseCase;
    private final GetOrderQuery getOrderQuery;

    public CustomerOrderController(
            CheckoutUseCase checkoutUseCase,
            GetOrderQuery getOrderQuery) {
        this.checkoutUseCase = checkoutUseCase;
        this.getOrderQuery = getOrderQuery;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDto> checkout(
            @RequestHeader("X-Customer-Id") String customerId,
            @RequestBody CheckoutRequest request,
            jakarta.servlet.http.HttpServletRequest httpRequest) {

        CheckoutCommand command = new CheckoutCommand(
                customerId,
                request.customerName(),
                request.contactEmail(),
                request.street(),
                request.number(),
                request.city(),
                request.postalCode(),
                request.country(),
                request.currency()
        );

        OrderId orderId = checkoutUseCase.checkout(command);

        String baseUrl = httpRequest.getScheme() + "://" +
                         httpRequest.getServerName() + ":" +
                         httpRequest.getServerPort() +
                         httpRequest.getContextPath();
        String trackingUrl = baseUrl + "/api/customer/orders/" + orderId.value().toString();

        CheckoutResponseDto response = new CheckoutResponseDto(
                orderId.value().toString(),
                "Order placed successfully",
                trackingUrl
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable String orderId,
            jakarta.servlet.http.HttpServletRequest request) {
        GetOrderQueryRequest query = new GetOrderQueryRequest(orderId);
        Order order = getOrderQuery.getOrder(query)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        OrderDto dto = toDto(order);
        return ResponseEntity.ok(dto);
    }

    private OrderDto toDto(Order order) {
        List<OrderItemDto> items = order.getItems().stream()
                .map(this::toDto)
                .toList();

        DeliveryAddressDto addressDto = new DeliveryAddressDto(
                order.getDeliveryAddress().street(),
                order.getDeliveryAddress().number(),
                order.getDeliveryAddress().city(),
                order.getDeliveryAddress().postalCode(),
                order.getDeliveryAddress().country()
        );

        CourierLocationDto courierLocationDto = null;
        if (order.getCourierLocation() != null) {
            courierLocationDto = new CourierLocationDto(
                    order.getCourierLocation().latitude(),
                    order.getCourierLocation().longitude(),
                    order.getCourierLocation().lastUpdated()
            );
        }

        return new OrderDto(
                order.getOrderId().value().toString(),
                order.getCustomerId().value().toString(),
                order.getRestaurantId().value().toString(),
                order.getCustomerName(),
                order.getContactEmail(),
                addressDto,
                items,
                order.getTotalAmount(),
                order.getPaymentTransactionId(),
                order.getOrderTime(),
                order.getStatus().name(),
                courierLocationDto
        );
    }

    private OrderItemDto toDto(OrderItem item) {
        return new OrderItemDto(
                item.getDishId().value().toString(),
                item.getDishName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }
}