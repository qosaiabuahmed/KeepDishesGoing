package be.kdg.prog6.restaurant.domain;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.events.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class Menu {
    private final MenuId id;
    private final RestaurantId restaurantId;
    private final List<Dish> dishes;
    private final List<Object> domainEvents = new ArrayList<>();

    public Menu(MenuId id, RestaurantId restaurantId, List<Dish> dishes) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.dishes = new ArrayList<>(dishes);
    }

    public static Menu createNew(RestaurantId restaurantId) {
        MenuId newId = MenuId.of(UUID.randomUUID().toString());
        return new Menu(newId, restaurantId, new ArrayList<>());
    }

    public DishId addNewDish(
            DishName name,
            DishType type,
            List<FoodTag> foodTags,
            Description description,
            Price price,
            ImageUrl imageUrl
    ) {
        Dish dish = Dish.createNew(name, type, foodTags, description, price, imageUrl);
        dishes.add(dish);
        domainEvents.add(new DishAddedEvent(this.restaurantId, dish.getId()));
        return dish.getId();
    }

    public void addExistingDish(Dish dish) {
        long publishedCount = dishes.stream()
                .filter(Dish::isVisible)
                .count();

        if (dish.isVisible() && publishedCount >= 10) {
            throw new IllegalStateException("Cannot have more than 10 visible dishes");
        }

        dishes.add(dish);
        domainEvents.add(new DishAddedEvent(this.restaurantId, dish.getId()));
    }

    public void addDish(Dish dish) {
        addExistingDish(dish);
    }

    public void removeDish(DishId dishId) {
        Dish dish = findDish(dishId);
        dishes.remove(dish);
        domainEvents.add(new DishRemovedEvent(this.restaurantId, dishId));
    }

    public void publishDish(DishId dishId) {
        long publishedCount = dishes.stream()
                .filter(Dish::isVisible)
                .count();

        if (publishedCount >= 10) {
            throw new IllegalStateException("Cannot have more than 10 visible dishes");
        }

        Dish dish = findDish(dishId);
        dish.publish();

        domainEvents.add(new DishPublishedEvent(
                dishId,
                this.restaurantId,
                dish.getName().value(),
                dish.getType().name(),
                dish.getFoodTags().stream().map(Enum::name).toList(),
                dish.getDescription().value(),
                dish.getPrice().amount(),
                dish.getImageUrl().value(),
                java.time.LocalDateTime.now()
        ));
    }

    public void unpublishDish(DishId dishId) {
        Dish dish = findDish(dishId);
        dish.unpublish();

        domainEvents.add(new DishUnpublishedEvent(
                dishId,
                this.restaurantId,
                java.time.LocalDateTime.now()
        ));
    }

    public void markDishOutOfStock(DishId dishId) {
        Dish dish = findDish(dishId);
        dish.markOutOfStock();

        domainEvents.add(new DishMarkedOutOfStockEvent(
                dishId,
                this.restaurantId,
                java.time.LocalDateTime.now()
        ));
    }

    public void markDishInStock(DishId dishId) {
        Dish dish = findDish(dishId);
        dish.markInStock();

        domainEvents.add(new DishMarkedInStockEvent(
                dishId,
                this.restaurantId,
                java.time.LocalDateTime.now()
        ));
    }

    public void updateDishDetails(
            DishId dishId,
            DishName name,
            DishType type,
            List<FoodTag> foodTags,
            Description description,
            Price price,
            ImageUrl imageUrl
    ) {
        Dish dish = findDish(dishId);
        dish.updateDetails(name, type, foodTags, description, price, imageUrl);
        domainEvents.add(new DishUpdatedEvent(dishId, this.restaurantId));
    }

    public Dish findDish(DishId dishId) {
        return dishes.stream()
                .filter(d -> d.getId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dish not found in menu"));
    }

    public long countPendingChanges() {
        return dishes.stream()
                .filter(Dish::hasPendingChanges)
                .count();
    }

    public void publishAllPendingChanges() {
        List<Dish> dishesWithPendingChanges = dishes.stream()
                .filter(Dish::hasPendingChanges)
                .toList();

        if (dishesWithPendingChanges.isEmpty()) {
            throw new IllegalStateException("No pending changes to publish");
        }

        long currentlyVisible = dishes.stream()
                .filter(Dish::isVisible)
                .count();

        long willBecomeVisible = dishesWithPendingChanges.stream()
                .filter(d -> d.getPublishStatus() == PublishStatus.DRAFT)
                .count();

        if (currentlyVisible + willBecomeVisible > 10) {
            throw new IllegalStateException("Publishing all pending changes would exceed the 10 dish limit");
        }

        for (Dish dish : dishesWithPendingChanges) {
            dish.publish();
            domainEvents.add(new DishPublishedEvent(
                    dish.getId(),
                    this.restaurantId,
                    dish.getName().value(),
                    dish.getType().name(),
                    dish.getFoodTags().stream().map(Enum::name).toList(),
                    dish.getDescription().value(),
                    dish.getPrice().amount(),
                    dish.getImageUrl().value(),
                    java.time.LocalDateTime.now()
            ));
        }
    }

    public List<Dish> getVisibleDishes() {
        return dishes.stream()
                .filter(Dish::isVisible)
                .toList();
    }

    public List<Dish> getAvailableDishes() {
        return dishes.stream()
                .filter(Dish::isAvailableForOrder)
                .toList();
    }

    public java.math.BigDecimal calculateAverageMenuPrice() {
        List<Dish> visibleDishes = getVisibleDishes();

        if (visibleDishes.isEmpty()) {
            return java.math.BigDecimal.ZERO;
        }

        java.math.BigDecimal total = visibleDishes.stream()
                .map(dish -> dish.getPrice().amount())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        return total.divide(
                java.math.BigDecimal.valueOf(visibleDishes.size()),
                2,
                java.math.RoundingMode.HALF_UP
        );
    }

    public PriceRange getPriceRange() {
        return PriceRange.fromAveragePrice(calculateAverageMenuPrice());
    }

    public MenuId getId() { return id; }
    public RestaurantId getRestaurantId() { return restaurantId; }
    public List<Dish> getDishes() { return Collections.unmodifiableList(dishes); }

    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}