package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.order.domain.BasketStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "baskets", schema = "customer")
public class BasketJpaEntity {
    @Id
    @Column(name = "basket_id")
    private UUID id;

    @Column(name = "customer_id", nullable = false, unique = true)
    private UUID customerId;

    @Column(name = "restaurant_id")
    private UUID restaurantId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BasketStatus status;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BasketItemJpaEntity> items = new ArrayList<>();

    protected BasketJpaEntity() {}

    public BasketJpaEntity(UUID id, UUID customerId, UUID restaurantId, BasketStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }


    public UUID getCustomerId() {
        return customerId;
    }


    public UUID getRestaurantId() {
        return restaurantId;
    }


    public BasketStatus getStatus() {
        return status;
    }


    public List<BasketItemJpaEntity> getItems() {
        return items;
    }

    public void setItems(List<BasketItemJpaEntity> items) {
        this.items = items;
    }
}