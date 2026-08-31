package be.kdg.prog6.order.domain;

import be.kdg.prog6.common.domain.DishId;

import java.math.BigDecimal;

public class OrderItem {
    private final DishId dishId;
    private final String dishName;
    private final BigDecimal unitPrice;
    private final int quantity;

    public OrderItem(DishId dishId, String dishName, BigDecimal unitPrice, int quantity) {
        if (dishId == null) {
            throw new IllegalArgumentException("Dish ID cannot be null");
        }
        if (dishName == null || dishName.isBlank()) {
            throw new IllegalArgumentException("Dish name cannot be empty");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price must be non-negative");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        this.dishId = dishId;
        this.dishName = dishName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public DishId getDishId() {
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