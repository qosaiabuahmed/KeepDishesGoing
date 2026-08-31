package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.order.domain.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders", schema = "customer")
public class OrderJpaEntity {
    @Id
    @Column(name = "order_id")
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "delivery_street", nullable = false)
    private String deliveryStreet;

    @Column(name = "delivery_number", nullable = false)
    private String deliveryNumber;

    @Column(name = "delivery_city", nullable = false)
    private String deliveryCity;

    @Column(name = "delivery_postal_code", nullable = false)
    private String deliveryPostalCode;

    @Column(name = "delivery_country", nullable = false)
    private String deliveryCountry;

    @Column(name = "payment_transaction_id", nullable = false)
    private String paymentTransactionId;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "courier_latitude")
    private Double courierLatitude;

    @Column(name = "courier_longitude")
    private Double courierLongitude;

    @Column(name = "courier_location_updated_at")
    private LocalDateTime courierLocationUpdatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItemJpaEntity> items = new ArrayList<>();

    protected OrderJpaEntity() {
    }

    public OrderJpaEntity(UUID id, UUID customerId, UUID restaurantId, String customerName, String contactEmail, String deliveryStreet, String deliveryNumber, String deliveryCity, String deliveryPostalCode, String deliveryCountry, String paymentTransactionId, LocalDateTime orderTime, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.customerName = customerName;
        this.contactEmail = contactEmail;
        this.deliveryStreet = deliveryStreet;
        this.deliveryNumber = deliveryNumber;
        this.deliveryCity = deliveryCity;
        this.deliveryPostalCode = deliveryPostalCode;
        this.deliveryCountry = deliveryCountry;
        this.paymentTransactionId = paymentTransactionId;
        this.orderTime = orderTime;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }


    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
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

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItemJpaEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderItemJpaEntity> items) {
        this.items = items;
    }

    public Double getCourierLatitude() {
        return courierLatitude;
    }

    public void setCourierLatitude(Double courierLatitude) {
        this.courierLatitude = courierLatitude;
    }

    public Double getCourierLongitude() {
        return courierLongitude;
    }

    public void setCourierLongitude(Double courierLongitude) {
        this.courierLongitude = courierLongitude;
    }

    public LocalDateTime getCourierLocationUpdatedAt() {
        return courierLocationUpdatedAt;
    }

    public void setCourierLocationUpdatedAt(LocalDateTime courierLocationUpdatedAt) {
        this.courierLocationUpdatedAt = courierLocationUpdatedAt;
    }
}