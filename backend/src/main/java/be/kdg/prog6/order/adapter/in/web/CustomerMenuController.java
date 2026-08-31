package be.kdg.prog6.order.adapter.in.web;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.DishProjection;
import be.kdg.prog6.order.port.in.GetDishesForCustomerQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/customer/restaurants/{restaurantId}/menu")
public class CustomerMenuController {

    private final GetDishesForCustomerQuery getDishesQuery;

    public CustomerMenuController(GetDishesForCustomerQuery getDishesQuery) {
        this.getDishesQuery = getDishesQuery;
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getMenu(
            @PathVariable String restaurantId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) List<String> foodTags,
            @RequestParam(required = false, defaultValue = "none") String sortBy
    ) {
        RestaurantId id = RestaurantId.of(restaurantId);
        List<DishProjection> projections = getDishesQuery.getAvailableDishes(id);

        java.util.stream.Stream<DishProjection> stream = projections.stream();

        if (type != null && !type.isBlank()) {
            stream = stream.filter(p -> p.getDishType().equalsIgnoreCase(type));
        }

        if (foodTags != null && !foodTags.isEmpty()) {
            stream = stream.filter(p ->
                    p.getFoodTags() != null &&
                    foodTags.stream().allMatch(tag ->
                            p.getFoodTags().stream()
                                    .anyMatch(dishTag -> dishTag.equalsIgnoreCase(tag))
                    )
            );
        }

        List<DishDto> dtos = stream
                .map(this::toDto)
                .collect(java.util.stream.Collectors.toList());

        if ("price".equalsIgnoreCase(sortBy)) {
            dtos.sort(java.util.Comparator.comparing(DishDto::price));
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            dtos.sort(java.util.Comparator.comparing(DishDto::price).reversed());
        } else if ("name".equalsIgnoreCase(sortBy)) {
            dtos.sort(java.util.Comparator.comparing(DishDto::name));
        }

        return ResponseEntity.ok(dtos);
    }

    private DishDto toDto(DishProjection projection) {
        return new DishDto(
                projection.getDishId().value().toString(),
                projection.getDishName(),
                projection.getDishType(),
                projection.getFoodTags(),
                projection.getDescription(),
                projection.getPrice(),
                projection.getImageUrl(),
                projection.getStatus().name()
        );
    }

    record DishDto(
            String id,
            String name,
            String type,
            List<String> foodTags,
            String description,
            BigDecimal price,
            String imageUrl,
            String stockStatus
    ) {}
}