package be.kdg.prog6.restaurant.adapter.out;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menus", schema = "restaurant")
public class MenuJpaEntity {
    @Id
    @Column(name = "menu_id")
    private UUID id;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DishJpaEntity> dishes = new ArrayList<>();

    protected MenuJpaEntity() {}

    public MenuJpaEntity(UUID id, UUID restaurantId) {
        this.id = id;
        this.restaurantId = restaurantId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
    public List<DishJpaEntity> getDishes() { return dishes; }
    public void setDishes(List<DishJpaEntity> dishes) { this.dishes = dishes; }
}