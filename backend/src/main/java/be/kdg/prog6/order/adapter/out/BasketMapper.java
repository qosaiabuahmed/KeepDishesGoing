package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.common.domain.BasketId;
import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.Basket;
import be.kdg.prog6.order.domain.BasketItem;
import be.kdg.prog6.order.domain.BasketStatus;

import java.util.stream.Collectors;

public class BasketMapper {

    public static BasketJpaEntity toJpaEntity(Basket basket) {
        BasketJpaEntity jpa = new BasketJpaEntity(
                basket.getId().value(),
                basket.getCustomerId().value(),
                basket.getRestaurantId() != null ? basket.getRestaurantId().value() : null,
                basket.getStatus()
        );

        var items = basket.getItems().stream()
                .map(item -> toJpaEntity(item, jpa))
                .collect(Collectors.toList());
        jpa.setItems(items);

        return jpa;
    }

    public static Basket toDomain(BasketJpaEntity jpa) {
        BasketId basketId = new BasketId(jpa.getId());
        CustomerId customerId = new CustomerId(jpa.getCustomerId());
        RestaurantId restaurantId = jpa.getRestaurantId() != null
                ? new RestaurantId(jpa.getRestaurantId())
                : null;
        BasketStatus status = jpa.getStatus();

        var items = jpa.getItems().stream()
                .map(BasketMapper::toDomain)
                .collect(Collectors.toList());

        return new Basket(basketId, customerId, restaurantId, items, status);
    }

    private static BasketItemJpaEntity toJpaEntity(BasketItem item, BasketJpaEntity basket) {
        return new BasketItemJpaEntity(
                basket,
                item.getDishId().value(),
                item.getDishName(),
                item.getUnitPrice(),
                item.getQuantity()
        );
    }

    private static BasketItem toDomain(BasketItemJpaEntity jpa) {
        DishId dishId = new DishId(jpa.getDishId());
        return new BasketItem(
                dishId,
                jpa.getDishName(),
                jpa.getUnitPrice(),
                jpa.getQuantity()
        );
    }
}