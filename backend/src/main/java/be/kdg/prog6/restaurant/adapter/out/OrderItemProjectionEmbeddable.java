package be.kdg.prog6.restaurant.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class OrderItemProjectionEmbeddable {

    @Column(name = "dish_id", nullable = false)
    private String dishId;

    @Column(name = "dish_name", nullable = false)
    private String dishName;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    protected OrderItemProjectionEmbeddable() {
    }

    public OrderItemProjectionEmbeddable(String dishId, String dishName, BigDecimal unitPrice, int quantity, BigDecimal totalPrice) {
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