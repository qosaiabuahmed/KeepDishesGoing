package be.kdg.prog6.restaurant.domain;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.events.OrderAcceptedEvent;
import be.kdg.prog6.common.events.OrderReadyForPickupEvent;
import be.kdg.prog6.common.events.OrderRejectedEvent;
import be.kdg.prog6.common.events.external.AddressDto;
import be.kdg.prog6.common.events.external.Coordinates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OrderProjection {
    private final OrderId orderId;
    private final RestaurantId restaurantId;
    private final CustomerId customerId;
    private final String customerName;
    private final String contactEmail;
    private final String deliveryStreet;
    private final String deliveryNumber;
    private final String deliveryCity;
    private final String deliveryPostalCode;
    private final String deliveryCountry;
    private final List<OrderItemProjection> items;
    private final BigDecimal totalAmount;
    private final String paymentTransactionId;
    private final LocalDateTime orderTime;
    private OrderProjectionStatus status;
    private String rejectionReason;
    private LocalDateTime decisionTime;
    private final List<Object> domainEvents = new ArrayList<>();

    public OrderProjection(
            OrderId orderId,
            RestaurantId restaurantId,
            CustomerId customerId,
            String customerName,
            String contactEmail,
            String deliveryStreet,
            String deliveryNumber,
            String deliveryCity,
            String deliveryPostalCode,
            String deliveryCountry,
            List<OrderItemProjection> items,
            BigDecimal totalAmount,
            String paymentTransactionId,
            LocalDateTime orderTime,
            OrderProjectionStatus status) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.contactEmail = contactEmail;
        this.deliveryStreet = deliveryStreet;
        this.deliveryNumber = deliveryNumber;
        this.deliveryCity = deliveryCity;
        this.deliveryPostalCode = deliveryPostalCode;
        this.deliveryCountry = deliveryCountry;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.paymentTransactionId = paymentTransactionId;
        this.orderTime = orderTime;
        this.status = status;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getDeliveryStreet() {
        return deliveryStreet;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public String getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    public String getDeliveryCountry() {
        return deliveryCountry;
    }

    public List<OrderItemProjection> getItems() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public OrderProjectionStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDateTime getDecisionTime() {
        return decisionTime;
    }

    public void setStatus(OrderProjectionStatus status) {
        this.status = status;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void setDecisionTime(LocalDateTime decisionTime) {
        this.decisionTime = decisionTime;
    }

    public void ensureBelongsToRestaurant(RestaurantId restaurantId) {
        if (!this.restaurantId.equals(restaurantId)) {
            throw new IllegalArgumentException("Order does not belong to this restaurant");
        }
    }

    public void acceptOrder(AddressDto pickupAddress, Coordinates pickupCoordinates,
                           AddressDto dropoffAddress, Coordinates dropoffCoordinates) {
        if (this.status != OrderProjectionStatus.SUBMITTED) {
            throw new IllegalStateException(
                    "Order can only be accepted when in SUBMITTED status. Current status: " + this.status
            );
        }

        this.status = OrderProjectionStatus.CONFIRMED;
        this.decisionTime = LocalDateTime.now();

        OrderAcceptedEvent event = new OrderAcceptedEvent(
                UUID.randomUUID(),
                LocalDateTime.now(),
                this.restaurantId.value(),
                this.orderId.value(),
                pickupAddress,
                pickupCoordinates,
                dropoffAddress,
                dropoffCoordinates
        );

        domainEvents.add(event);
    }

    public void rejectOrder(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason cannot be empty");
        }

        if (this.status != OrderProjectionStatus.SUBMITTED) {
            throw new IllegalStateException(
                    "Order can only be rejected when in SUBMITTED status. Current status: " + this.status
            );
        }

        this.status = OrderProjectionStatus.CANCELLED;
        this.rejectionReason = reason;
        this.decisionTime = LocalDateTime.now();

        OrderRejectedEvent event = new OrderRejectedEvent(
                this.orderId,
                this.restaurantId,
                reason,
                LocalDateTime.now()
        );

        domainEvents.add(event);
    }

    public void markOrderReady() {
        if (this.status != OrderProjectionStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Order can only be marked ready when in CONFIRMED status. Current status: " + this.status
            );
        }

        this.status = OrderProjectionStatus.READY;

        OrderReadyForPickupEvent event = new OrderReadyForPickupEvent(
                UUID.randomUUID(),
                LocalDateTime.now(),
                this.restaurantId.value(),
                this.orderId.value()
        );

        domainEvents.add(event);
    }

    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public static class OrderItemProjection {
        private final String dishId;
        private final String dishName;
        private final BigDecimal unitPrice;
        private final int quantity;
        private final BigDecimal totalPrice;

        public OrderItemProjection(String dishId, String dishName, BigDecimal unitPrice, int quantity, BigDecimal totalPrice) {
            this.dishId = dishId;
            this.dishName = dishName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        public String getDishId() {
            return dishId;
        }

        public String getDishName() {
            return dishName;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }
    }
}