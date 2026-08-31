package be.kdg.prog6.order.domain;

import be.kdg.prog6.common.domain.DishId;

import java.math.BigDecimal;
import java.util.Objects;

public class BasketItem {
    private final DishId dishId;
    private final String dishName;
    private final BigDecimal unitPrice;
    private int quantity;

    public BasketItem(DishId dishId, String dishName, BigDecimal unitPrice, int quantity) {
        if (dishId == null) {
            throw new IllegalArgumentException("Dish ID cannot be null");
        }
        if (dishName == null || dishName.isBlank()) {
            throw new IllegalArgumentException("Dish name cannot be empty");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        this.dishId = dishId;
        this.dishName = dishName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = newQuantity;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketItem that)) return false;
        return Objects.equals(dishId, that.dishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishId);
    }
}
