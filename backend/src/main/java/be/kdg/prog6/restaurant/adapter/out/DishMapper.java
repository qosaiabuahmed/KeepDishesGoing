package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.StockStatus;
import be.kdg.prog6.restaurant.domain.*;

import java.util.stream.Collectors;

public class DishMapper {


    public static DishJpaEntity toJpaEntity(Dish dish, MenuJpaEntity menu) {
        return new DishJpaEntity(
                dish.getId().value(),
                menu,
                menu.getRestaurantId(),
                dish.getName().value(),
                dish.getType().name(),
                dish.getFoodTags().stream().map(Enum::name).collect(Collectors.toList()),
                dish.getDescription().value(),
                dish.getPrice().amount(),
                dish.getImageUrl().value(),
                dish.getDraftName().value(),
                dish.getDraftType().name(),
                dish.getDraftFoodTags().stream().map(Enum::name).collect(Collectors.toList()),
                dish.getDraftDescription().value(),
                dish.getDraftPrice().amount(),
                dish.getDraftImageUrl().value(),
                dish.getPublishStatus().name(),
                dish.getStockStatus().name(),
                dish.hasPendingChanges()
        );
    }


    public static Dish toDomain(DishJpaEntity jpa) {
        return new Dish(
                new DishId(jpa.getId()),
                new DishName(jpa.getPublishedName()),
                DishType.valueOf(jpa.getPublishedType()),
                jpa.getPublishedFoodTags().stream()
                        .map(FoodTag::valueOf)
                        .collect(Collectors.toList()),
                new Description(jpa.getPublishedDescription()),
                new Price(jpa.getPublishedPrice()),
                new ImageUrl(jpa.getPublishedImageUrl()),
                new DishName(jpa.getDraftName()),
                DishType.valueOf(jpa.getDraftType()),
                jpa.getDraftFoodTags().stream()
                        .map(FoodTag::valueOf)
                        .collect(Collectors.toList()),
                new Description(jpa.getDraftDescription()),
                new Price(jpa.getDraftPrice()),
                new ImageUrl(jpa.getDraftImageUrl()),
                PublishStatus.valueOf(jpa.getPublishStatus()),
                StockStatus.valueOf(jpa.getStockStatus()),
                jpa.isHasPendingChanges()
        );
    }
}