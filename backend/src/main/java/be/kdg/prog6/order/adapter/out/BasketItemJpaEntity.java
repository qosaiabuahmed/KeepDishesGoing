package be.kdg.prog6.order.adapter.out;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "basket_items", schema = "customer")
public class BasketItemJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "basket_item_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id", nullable = false)
    private BasketJpaEntity basket;

    @Column(name = "dish_id", nullable = false)
    private UUID dishId;

    @Column(name = "dish_name", nullable = false)
    private String dishName;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected BasketItemJpaEntity() {}

    public BasketItemJpaEntity(BasketJpaEntity basket, UUID dishId, String dishName,
                               BigDecimal unitPrice, int quantity) {
        this.basket = basket;
        this.dishId = dishId;
        this.dishName = dishName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }


    public BasketJpaEntity getBasket() {
        return basket;
    }

    public void setBasket(BasketJpaEntity basket) {
        this.basket = basket;
    }

    public UUID getDishId() {
        return dishId;
    }

    public void setDishId(UUID dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}