package be.kdg.prog6.order.domain;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.domain.StockStatus;

import java.math.BigDecimal;
import java.util.List;

public class DishProjection {
    private final DishId dishId;
    private final RestaurantId restaurantId;
    private String dishName;
    private String dishType;
    private List<String> foodTags;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private StockStatus status;



    public DishProjection(
            DishId dishId,
            RestaurantId restaurantId,
            String dishName,
            String dishType,
            List<String> foodTags,
            String description,
            BigDecimal price,
            String imageUrl,
            StockStatus status) {
        this.dishId = dishId;
        this.restaurantId = restaurantId;
        this.dishName = dishName;
        this.dishType = dishType;
        this.foodTags = foodTags;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public DishId getDishId() { return dishId; }
    public RestaurantId getRestaurantId() { return restaurantId; }
    public String getDishName() { return dishName; }
    public String getDishType() { return dishType; }
    public List<String> getFoodTags() { return foodTags; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public StockStatus getStatus() { return status; }

    public void markOutOfStock() {
        if (this.status == StockStatus.OUT_OF_STOCK) {
            return;
        }
        this.status = StockStatus.OUT_OF_STOCK;
    }

    public void markInStock() {
        if (this.status == StockStatus.IN_STOCK) {
            return;
        }
        this.status = StockStatus.IN_STOCK;
    }

}