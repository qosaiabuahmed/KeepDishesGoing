package be.kdg.prog6.order.adapter.out;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dish_projections", schema = "customer")
public class DishProjectionJpaEntity {

    @Id
    private UUID dishId;

    private UUID restaurantId;
    private String dishName;
    private String dishType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dish_projection_food_tags",
            schema = "customer",
            joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "food_tag")
    private List<String> foodTags;

    @Column(length = 1000)
    private String description;

    private BigDecimal price;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private StockStatus status;

    public enum StockStatus {
        IN_STOCK,
        OUT_OF_STOCK
    }

    public DishProjectionJpaEntity() {}

    public DishProjectionJpaEntity(
            UUID dishId,
            UUID restaurantId,
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

    public UUID getDishId() { return dishId; }

    public UUID getRestaurantId() { return restaurantId; }

    public String getDishName() { return dishName; }

    public String getDishType() { return dishType; }

    public List<String> getFoodTags() { return foodTags; }

    public String getDescription() { return description; }

    public BigDecimal getPrice() { return price; }

    public String getImageUrl() { return imageUrl; }

    public StockStatus getStatus() { return status; }
}