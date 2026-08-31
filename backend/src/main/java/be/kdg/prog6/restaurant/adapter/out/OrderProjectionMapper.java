package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.domain.OrderProjectionStatus;

import java.util.UUID;
import java.util.stream.Collectors;

public class OrderProjectionMapper {

    public static OrderProjection toDomain(OrderProjectionJpaEntity entity) {
        return new OrderProjection(
                new OrderId(entity.getOrderId()),
                new RestaurantId(entity.getRestaurantId()),
                new CustomerId(entity.getCustomerId()),
                entity.getCustomerName(),
                entity.getContactEmail(),
                entity.getDeliveryStreet(),
                entity.getDeliveryNumber(),
                entity.getDeliveryCity(),
                entity.getDeliveryPostalCode(),
                entity.getDeliveryCountry(),
                entity.getItems().stream()
                        .map(item -> new OrderProjection.OrderItemProjection(
                                item.getDishId(),
                                item.getDishName(),
                                item.getUnitPrice(),
                                item.getQuantity(),
                                item.getTotalPrice()
                        ))
                        .collect(Collectors.toList()),
                entity.getTotalAmount(),
                entity.getPaymentTransactionId(),
                entity.getOrderTime(),
                OrderProjectionStatus.valueOf(entity.getStatus())
        );
    }

    public static OrderProjectionJpaEntity toJpaEntity(OrderProjection projection) {
        OrderProjectionJpaEntity entity = new OrderProjectionJpaEntity(
                projection.getOrderId().value(),
                projection.getRestaurantId().value(),
                projection.getCustomerId().value(),
                projection.getCustomerName(),
                projection.getContactEmail(),
                projection.getDeliveryStreet(),
                projection.getDeliveryNumber(),
                projection.getDeliveryCity(),
                projection.getDeliveryPostalCode(),
                projection.getDeliveryCountry(),
                projection.getItems().stream()
                        .map(item -> new OrderItemProjectionEmbeddable(
                                item.getDishId(),
                                item.getDishName(),
                                item.getUnitPrice(),
                                item.getQuantity(),
                                item.getTotalPrice()
                        ))
                        .collect(Collectors.toList()),
                projection.getTotalAmount(),
                projection.getPaymentTransactionId(),
                projection.getOrderTime(),
                projection.getStatus().name()
        );

        entity.setRejectionReason(projection.getRejectionReason());
        entity.setDecisionTime(projection.getDecisionTime());

        return entity;
    }
}