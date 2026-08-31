package be.kdg.prog6.restaurant.adapter.out;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_projections", schema = "restaurant")
public class OrderProjectionJpaEntity {

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

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

    @ElementCollection
    @CollectionTable(
            name = "order_projection_items",
            schema = "restaurant",
            joinColumns = @JoinColumn(name = "order_id")
    )
    private List<OrderItemProjectionEmbeddable> items = new ArrayList<>();

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_transaction_id", nullable = false)
    private String paymentTransactionId;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "decision_time")
    private LocalDateTime decisionTime;

    protected OrderProjectionJpaEntity() {
    }

    public OrderProjectionJpaEntity(
            UUID orderId,
            UUID restaurantId,
            UUID customerId,
            String customerName,
            String contactEmail,
            String deliveryStreet,
            String deliveryNumber,
            String deliveryCity,
            String deliveryPostalCode,
            String deliveryCountry,
            List<OrderItemProjectionEmbeddable> items,
            BigDecimal totalAmount,
            String paymentTransactionId,
            LocalDateTime orderTime,
            String status) {
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

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public UUID getCustomerId() {
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

    public List<OrderItemProjectionEmbeddable> getItems() {
        return items;
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

    public String getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDateTime getDecisionTime() {
        return decisionTime;
    }


    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void setDecisionTime(LocalDateTime decisionTime) {
        this.decisionTime = decisionTime;
    }
}