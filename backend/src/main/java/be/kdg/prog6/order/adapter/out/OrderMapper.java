package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.DeliveryAddress;
import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.domain.OrderItem;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity jpa = new OrderJpaEntity(
                order.getOrderId().value(),
                order.getCustomerId().value(),
                order.getRestaurantId().value(),
                order.getCustomerName(),
                order.getContactEmail(),
                order.getDeliveryAddress().street(),
                order.getDeliveryAddress().number(),
                order.getDeliveryAddress().city(),
                order.getDeliveryAddress().postalCode(),
                order.getDeliveryAddress().country(),
                order.getPaymentTransactionId(),
                order.getOrderTime(),
                order.getStatus()
        );

        if (order.getCourierLocation() != null) {
            jpa.setCourierLatitude(order.getCourierLocation().latitude());
            jpa.setCourierLongitude(order.getCourierLocation().longitude());
            jpa.setCourierLocationUpdatedAt(order.getCourierLocation().lastUpdated());
        }

        var items = order.getItems().stream()
                .map(item -> toJpaEntity(item, jpa))
                .collect(Collectors.toList());
        jpa.setItems(items);

        return jpa;
    }

    public static Order toDomain(OrderJpaEntity jpa) {
        OrderId orderId = new OrderId(jpa.getId());
        CustomerId customerId = new CustomerId(jpa.getCustomerId());
        RestaurantId restaurantId = new RestaurantId(jpa.getRestaurantId());

        DeliveryAddress deliveryAddress = new DeliveryAddress(
                jpa.getDeliveryStreet(),
                jpa.getDeliveryNumber(),
                jpa.getDeliveryCity(),
                jpa.getDeliveryPostalCode(),
                jpa.getDeliveryCountry()
        );

        var items = jpa.getItems().stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());

        Order order = new Order(
                orderId,
                customerId,
                restaurantId,
                items,
                deliveryAddress,
                jpa.getCustomerName(),
                jpa.getContactEmail(),
                jpa.getPaymentTransactionId(),
                jpa.getOrderTime(),
                jpa.getStatus()
        );

        if (jpa.getCourierLatitude() != null && jpa.getCourierLongitude() != null) {
            order.updateCourierLocation(jpa.getCourierLatitude(), jpa.getCourierLongitude());
        }

        return order;
    }

    private static OrderItemJpaEntity toJpaEntity(OrderItem item, OrderJpaEntity order) {
        return new OrderItemJpaEntity(
                order,
                item.getDishId().value(),
                item.getDishName(),
                item.getUnitPrice(),
                item.getQuantity()
        );
    }

    private static OrderItem toDomain(OrderItemJpaEntity jpa) {
        DishId dishId = new DishId(jpa.getDishId());
        return new OrderItem(
                dishId,
                jpa.getDishName(),
                jpa.getUnitPrice(),
                jpa.getQuantity()
        );
    }
}