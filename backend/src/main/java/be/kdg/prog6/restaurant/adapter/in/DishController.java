package be.kdg.prog6.restaurant.adapter.in;

import be.kdg.prog6.restaurant.adapter.in.dto.DishDto;
import be.kdg.prog6.restaurant.adapter.in.request.CreateDishRequest;
import be.kdg.prog6.restaurant.adapter.in.request.SchedulePublicationRequest;
import be.kdg.prog6.restaurant.adapter.in.request.UpdateDishRequest;
import be.kdg.prog6.restaurant.domain.Dish;
import be.kdg.prog6.restaurant.domain.Menu;
import be.kdg.prog6.restaurant.domain.ScheduledPublicationId;
import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.port.in.*;
import be.kdg.prog6.restaurant.port.out.LoadMenuPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/dishes")
public class DishController {

    private final CreateDishUseCase createDishUseCase;
    private final UpdateDishUseCase updateDishUseCase;
    private final PublishDishUseCase publishDishUseCase;
    private final UnpublishDishUseCase unpublishDishUseCase;
    private final MarkDishStockUseCase markDishStockUseCase;
    private final PublishAllPendingChangesUseCase publishAllPendingChangesUseCase;
    private final SchedulePublicationUseCase schedulePublicationUseCase;
    private final LoadMenuPort loadMenuPort;

    public DishController(
            CreateDishUseCase createDishUseCase,
            UpdateDishUseCase updateDishUseCase,
            PublishDishUseCase publishDishUseCase,
            UnpublishDishUseCase unpublishDishUseCase,
            MarkDishStockUseCase markDishStockUseCase,
            PublishAllPendingChangesUseCase publishAllPendingChangesUseCase,
            SchedulePublicationUseCase schedulePublicationUseCase,
            LoadMenuPort loadMenuPort) {
        this.createDishUseCase = createDishUseCase;
        this.updateDishUseCase = updateDishUseCase;
        this.publishDishUseCase = publishDishUseCase;
        this.unpublishDishUseCase = unpublishDishUseCase;
        this.markDishStockUseCase = markDishStockUseCase;
        this.publishAllPendingChangesUseCase = publishAllPendingChangesUseCase;
        this.schedulePublicationUseCase = schedulePublicationUseCase;
        this.loadMenuPort = loadMenuPort;
    }

    @PostMapping
    public ResponseEntity<Void> createDish(
            @PathVariable UUID restaurantId,
            @RequestBody CreateDishRequest request) {

        CreateDishCommand command = new CreateDishCommand(
                restaurantId.toString(),
                request.name(),
                request.type(),
                request.foodTags(),
                request.description(),
                request.price(),
                request.imageUrl()
        );

        DishId dishId = createDishUseCase.createDish(command);

        return ResponseEntity.created(
                URI.create("/api/restaurants/" + restaurantId + "/dishes/" + dishId.value())
        ).build();
    }

    @PutMapping("/{dishId}")
    public ResponseEntity<Void> updateDish(
            @PathVariable UUID restaurantId,
            @PathVariable UUID dishId,
            @RequestBody UpdateDishRequest request) {

        UpdateDishCommand command = new UpdateDishCommand(
                restaurantId.toString(),
                dishId.toString(),
                request.name(),
                request.type(),
                request.foodTags(),
                request.description(),
                request.price(),
                request.imageUrl()
        );

        updateDishUseCase.updateDish(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dishId}/publish")
    public ResponseEntity<Void> publishDish(
            @PathVariable UUID restaurantId,
            @PathVariable UUID dishId) {

        PublishDishCommand command = new PublishDishCommand(
                restaurantId.toString(),
                dishId.toString()
        );

        publishDishUseCase.publishDish(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dishId}/unpublish")
    public ResponseEntity<Void> unpublishDish(
            @PathVariable UUID restaurantId,
            @PathVariable UUID dishId) {

        UnpublishDishCommand command = new UnpublishDishCommand(
                restaurantId.toString(),
                dishId.toString()
        );

        unpublishDishUseCase.unpublishDish(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/publish-all-pending")
    public ResponseEntity<Void> publishAllPendingChanges(@PathVariable("restaurantId") UUID restaurantId) {
        PublishAllPendingChangesCommand command = new PublishAllPendingChangesCommand(
                restaurantId.toString()
        );

        publishAllPendingChangesUseCase.publishAllPendingChanges(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-publication")
    public ResponseEntity<Void> schedulePublication(
            @PathVariable UUID restaurantId,
            @RequestBody SchedulePublicationRequest request) {

        SchedulePublicationCommand command = new SchedulePublicationCommand(
                restaurantId.toString(),
                request.dishIdsToPublish() != null ? request.dishIdsToPublish() : List.of(),
                request.dishIdsToUnpublish() != null ? request.dishIdsToUnpublish() : List.of(),
                request.scheduledTime()
        );

        ScheduledPublicationId scheduledPublicationId = schedulePublicationUseCase.schedulePublication(command);

        return ResponseEntity.created(
                URI.create("/api/restaurants/" + restaurantId + "/scheduled-publications/" + scheduledPublicationId.value())
        ).build();
    }

    @PostMapping("/{dishId}/out-of-stock")
    public ResponseEntity<Void> markOutOfStock(
            @PathVariable UUID restaurantId,
            @PathVariable UUID dishId) {

        MarkDishOutOfStockCommand command = new MarkDishOutOfStockCommand(
                restaurantId.toString(),
                dishId.toString()
        );

        markDishStockUseCase.markOutOfStock(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dishId}/in-stock")
    public ResponseEntity<Void> markInStock(
            @PathVariable UUID restaurantId,
            @PathVariable UUID dishId) {

        MarkDishInStockCommand command = new MarkDishInStockCommand(
                restaurantId.toString(),
                dishId.toString()
        );

        markDishStockUseCase.markInStock(command);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getAllDishes(
            @PathVariable UUID restaurantId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) List<String> foodTags,
            @RequestParam(required = false, defaultValue = "none") String sortBy
    ) {
        Menu menu = loadMenuPort.loadByRestaurantId(new RestaurantId(restaurantId))
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        java.util.stream.Stream<Dish> dishStream = menu.getDishes().stream();


        if (type != null && !type.isBlank()) {
            dishStream = dishStream.filter(dish -> dish.getType().name().equalsIgnoreCase(type));
        }


        if (foodTags != null && !foodTags.isEmpty()) {
            dishStream = dishStream.filter(dish ->
                    foodTags.stream().allMatch(tag ->
                            dish.getFoodTags().stream()
                                    .anyMatch(dishTag -> dishTag.name().equalsIgnoreCase(tag))
                    )
            );
        }


        java.util.List<DishDto> dishes = dishStream
                .map(this::toDishDto)
                .collect(java.util.stream.Collectors.toList());


        if ("price".equalsIgnoreCase(sortBy)) {
            dishes.sort(java.util.Comparator.comparing(DishDto::price));
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            dishes.sort(java.util.Comparator.comparing(DishDto::price).reversed());
        } else if ("name".equalsIgnoreCase(sortBy)) {
            dishes.sort(java.util.Comparator.comparing(DishDto::name));
        }

        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<DishDto> getDish(
            @PathVariable UUID restaurantId,
            @PathVariable UUID dishId) {

        Menu menu = loadMenuPort.loadByRestaurantId(new RestaurantId(restaurantId))
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        Dish dish = menu.findDish(new DishId(dishId));

        return ResponseEntity.ok(toDishDto(dish));
    }

    @GetMapping("/pending-changes/count")
    public ResponseEntity<Long> getPendingChangesCount(@PathVariable UUID restaurantId) {
        Menu menu = loadMenuPort.loadByRestaurantId(new RestaurantId(restaurantId))
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        long count = menu.countPendingChanges();

        return ResponseEntity.ok(count);
    }

    private DishDto toDishDto(Dish dish) {
        return new DishDto(
                dish.getId().value(),
                dish.getName().value(),
                dish.getType().name(),
                dish.getFoodTags().stream()
                        .map(Enum::name)
                        .toList(),
                dish.getDescription().value(),
                dish.getPrice().amount(),
                dish.getImageUrl().value(),
                dish.getDraftName().value(),
                dish.getDraftType().name(),
                dish.getDraftFoodTags().stream()
                        .map(Enum::name)
                        .toList(),
                dish.getDraftDescription().value(),
                dish.getDraftPrice().amount(),
                dish.getDraftImageUrl().value(),
                dish.getPublishStatus().name(),
                dish.getStockStatus().name(),
                dish.hasPendingChanges(),
                dish.isAvailableForOrder(),
                dish.isVisible()
        );
    }
}