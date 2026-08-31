package be.kdg.prog6.order.domain;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.events.OrderRejectedEvent;
import be.kdg.prog6.common.events.OrderSubmittedEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    public static final int AUTO_DECLINE_MINUTES = 5;
    public static final String AUTO_DECLINE_REASON = "No response from restaurant within 5 minutes";

    private final OrderId orderId;
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final List<OrderItem> items;
    private final DeliveryAddress deliveryAddress;
    private final String customerName;
    private final String contactEmail;
    private final LocalDateTime orderTime;
    private final String paymentTransactionId;
    private OrderStatus status;
    private CourierLocation courierLocation;
    private final List<Object> domainEvents = new ArrayList<>();

    public Order(OrderId orderId, CustomerId customerId, RestaurantId restaurantId,
                 List<OrderItem> items, DeliveryAddress deliveryAddress,
                 String customerName, String contactEmail, String paymentTransactionId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant ID cannot be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        if (deliveryAddress == null) {
            throw new IllegalArgumentException("Delivery address cannot be null");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (contactEmail == null || contactEmail.isBlank()) {
            throw new IllegalArgumentException("Contact email cannot be empty");
        }
        if (paymentTransactionId == null || paymentTransactionId.isBlank()) {
            throw new IllegalArgumentException("Payment transaction ID cannot be empty");
        }

        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.customerName = customerName;
        this.contactEmail = contactEmail;
        this.paymentTransactionId = paymentTransactionId;
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.SUBMITTED;

        List<OrderSubmittedEvent.OrderItemDto> eventItems = this.items.stream()
                .map(item -> new OrderSubmittedEvent.OrderItemDto(
                        item.getDishId().value().toString(),
                        item.getDishName(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getTotalPrice()
                ))
                .collect(Collectors.toList());

        OrderSubmittedEvent.DeliveryAddressDto addressDto = new OrderSubmittedEvent.DeliveryAddressDto(
                this.deliveryAddress.street(),
                this.deliveryAddress.number(),
                this.deliveryAddress.city(),
                this.deliveryAddress.postalCode(),
                this.deliveryAddress.country()
        );

        domainEvents.add(new OrderSubmittedEvent(
                this.orderId,
                this.restaurantId,
                this.customerId,
                this.customerName,
                this.contactEmail,
                addressDto,
                eventItems,
                getTotalAmount(),
                this.paymentTransactionId,
                this.orderTime,
                LocalDateTime.now()
        ));
    }

    public Order(OrderId orderId, CustomerId customerId, RestaurantId restaurantId,
                 List<OrderItem> items, DeliveryAddress deliveryAddress,
                 String customerName, String contactEmail, String paymentTransactionId,
                 LocalDateTime orderTime, OrderStatus status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.customerName = customerName;
        this.contactEmail = contactEmail;
        this.paymentTransactionId = paymentTransactionId;
        this.orderTime = orderTime;
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
    }

    public void updateCourierLocation(Double latitude, Double longitude) {
        this.courierLocation = new CourierLocation(latitude, longitude, LocalDateTime.now());
    }

    public boolean shouldBeAutoDeclined(LocalDateTime currentTime) {
        if (this.status != OrderStatus.SUBMITTED) {
            return false;
        }
        LocalDateTime threshold = currentTime.minusMinutes(AUTO_DECLINE_MINUTES);
        return this.orderTime.isBefore(threshold);
    }

    public void autoDecline(String reason) {
        if (this.status != OrderStatus.SUBMITTED) {
            throw new IllegalStateException(
                    "Order can only be auto-declined when in SUBMITTED status. Current status: " + this.status
            );
        }

        this.status = OrderStatus.CANCELLED;

        OrderRejectedEvent event = new OrderRejectedEvent(
                this.orderId,
                this.restaurantId,
                reason,
                LocalDateTime.now()
        );

        domainEvents.add(event);
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public CourierLocation getCourierLocation() {
        return courierLocation;
    }

    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}