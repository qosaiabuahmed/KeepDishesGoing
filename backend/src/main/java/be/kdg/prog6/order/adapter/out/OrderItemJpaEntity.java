package be.kdg.prog6.order.adapter.out;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items", schema = "customer")
public class OrderItemJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpaEntity order;

    @Column(name = "dish_id", nullable = false)
    private UUID dishId;

    @Column(name = "dish_name", nullable = false)
    private String dishName;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected OrderItemJpaEntity() {}

    public OrderItemJpaEntity(OrderJpaEntity order, UUID dishId, String dishName,
                              BigDecimal unitPrice, int quantity) {
        this.order = order;
        this.dishId = dishId;
        this.dishName = dishName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }


    public OrderJpaEntity getOrder() {
        return order;
    }


    public UUID getDishId() {
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

}