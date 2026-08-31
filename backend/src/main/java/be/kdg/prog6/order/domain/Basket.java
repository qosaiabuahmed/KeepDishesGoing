package be.kdg.prog6.order.domain;

import be.kdg.prog6.common.domain.BasketId;
import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.domain.StockStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Basket {
    private final BasketId id;
    private final CustomerId customerId;
    private RestaurantId restaurantId;
    private final List<BasketItem> items;
    private BasketStatus status;
    private final List<Object> domainEvents = new ArrayList<>();

    public Basket(BasketId id, CustomerId customerId, RestaurantId restaurantId,
                  List<BasketItem> items, BasketStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = new ArrayList<>(items);
        this.status = status;
    }

    public static Basket createNew(CustomerId customerId) {
        return new Basket(
                BasketId.newId(),
                customerId,
                null,
                new ArrayList<>(),
                BasketStatus.ACTIVE
        );
    }

    public void addDish(RestaurantId dishRestaurantId, DishId dishId,
                       String dishName, BigDecimal unitPrice, int quantity) {
        if (restaurantId == null) {
            this.restaurantId = dishRestaurantId;
        } else if (!restaurantId.equals(dishRestaurantId)) {
            throw new IllegalStateException(
                    "Cannot add dishes from different restaurants. " +
                    "Clear basket to order from a different restaurant."
            );
        }

        Optional<BasketItem> existingItem = items.stream()
                .filter(item -> item.getDishId().equals(dishId))
                .findFirst();

        if (existingItem.isPresent()) {
            int newQuantity = existingItem.get().getQuantity() + quantity;
            existingItem.get().updateQuantity(newQuantity);
        } else {
            BasketItem newItem = new BasketItem(dishId, dishName, unitPrice, quantity);
            items.add(newItem);
        }


    }

    public void removeDish(DishId dishId) {
        BasketItem item = items.stream()
                .filter(i -> i.getDishId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dish not found in basket"));

        items.remove(item);

        if (items.isEmpty()) {
            this.restaurantId = null;
        }

    }

    public void updateQuantity(DishId dishId, int newQuantity) {
        BasketItem item = items.stream()
                .filter(i -> i.getDishId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dish not found in basket"));

        item.updateQuantity(newQuantity);
    }

    public void clear() {
        items.clear();
        this.restaurantId = null;

    }

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(BasketItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalItemCount() {
        return items.stream()
                .mapToInt(BasketItem::getQuantity)
                .sum();
    }

    public BasketValidationResult validate(Function<DishId, Optional<DishProjection>> dishLoader) {
        if (isEmpty()) {
            return BasketValidationResult.valid();
        }

        List<BasketValidationResult.DishValidationIssue> issues = new ArrayList<>();

        for (BasketItem item : items) {
            Optional<DishProjection> dishOpt = dishLoader.apply(item.getDishId());

            if (dishOpt.isEmpty()) {
                issues.add(new BasketValidationResult.DishValidationIssue(
                        item.getDishId(),
                        item.getDishName(),
                        BasketValidationResult.ValidationIssueType.DISH_NOT_FOUND,
                        "Dish is no longer available"
                ));
            } else {
                DishProjection dish = dishOpt.get();

                if (dish.getStatus() == StockStatus.OUT_OF_STOCK) {
                    issues.add(new BasketValidationResult.DishValidationIssue(
                            item.getDishId(),
                            item.getDishName(),
                            BasketValidationResult.ValidationIssueType.DISH_OUT_OF_STOCK,
                            "Dish is currently out of stock"
                    ));
                }
            }
        }

        if (issues.isEmpty()) {
            return BasketValidationResult.valid();
        } else {
            return BasketValidationResult.invalid(issues);
        }
    }

    public BasketId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public List<BasketItem> getItems() {
        return List.copyOf(items);
    }

    public BasketStatus getStatus() {
        return status;
    }

    public void validateForCheckout(RestaurantProjection restaurant) {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot checkout with empty basket");
        }

        if (!restaurant.isOpen()) {
            throw new IllegalStateException("Cannot checkout: restaurant is currently closed");
        }
    }

    public List<Object> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}