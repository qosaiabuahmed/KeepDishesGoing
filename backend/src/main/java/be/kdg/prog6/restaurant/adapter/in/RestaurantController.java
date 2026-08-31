package be.kdg.prog6.restaurant.adapter.in;

import be.kdg.prog6.restaurant.adapter.in.dto.RestaurantDto;
import be.kdg.prog6.restaurant.adapter.in.dto.RestaurantDto.AddressDto;
import be.kdg.prog6.restaurant.adapter.in.dto.RestaurantDto.OpeningHoursDto;
import be.kdg.prog6.restaurant.adapter.in.request.CreateRestaurantRequest;
import be.kdg.prog6.restaurant.adapter.in.security.CurrentUserProvider;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.port.in.*;
import be.kdg.prog6.restaurant.port.out.LoadMenuPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetAllRestaurantsUseCase getAllRestaurantsUseCase;
    private final GetRestaurantByIdUseCase getRestaurantByIdUseCase;
    private final OpenRestaurantUseCase openRestaurantUseCase;
    private final CloseRestaurantUseCase closeRestaurantUseCase;
    private final LoadMenuPort loadMenuPort;
    private final CurrentUserProvider currentUserProvider;

    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            GetAllRestaurantsUseCase getAllRestaurantsUseCase,
            GetRestaurantByIdUseCase getRestaurantByIdUseCase,
            OpenRestaurantUseCase openRestaurantUseCase,
            CloseRestaurantUseCase closeRestaurantUseCase,
            LoadMenuPort loadMenuPort,
            CurrentUserProvider currentUserProvider) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.getAllRestaurantsUseCase = getAllRestaurantsUseCase;
        this.getRestaurantByIdUseCase = getRestaurantByIdUseCase;
        this.openRestaurantUseCase = openRestaurantUseCase;
        this.closeRestaurantUseCase = closeRestaurantUseCase;
        this.loadMenuPort = loadMenuPort;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    public ResponseEntity<Void> createRestaurant(@RequestBody CreateRestaurantRequest request) {
        String ownerId = currentUserProvider.getCurrentUserId();

        CreateRestaurantCommand command = new CreateRestaurantCommand(
                ownerId,
                request.name(),
                request.cuisine(),
                request.address(),
                request.contactEmail(),
                request.imageUrls(),
                request.defaultPrepTimeMinutes(),
                request.openingHours()
        );

        RestaurantId restaurantId = this.createRestaurantUseCase.createRestaurant(command);

        return ResponseEntity.created(
                URI.create("/api/restaurants/" + restaurantId.value())
        ).build();
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> findAll() {
        String ownerId = currentUserProvider.getCurrentUserId();

        List<RestaurantDto> restaurantDtos =
                this.getAllRestaurantsUseCase.getAllRestaurants()
                        .stream()
                        .filter(restaurant -> restaurant.getOwnerId().value().toString().equals(ownerId))
                        .map(restaurant -> {
                            int dishCount = loadMenuPort.loadByRestaurantId(restaurant.getId())
                                    .map(menu -> menu.getDishes().size())
                                    .orElse(0);

                            return new RestaurantDto(
                                    restaurant.getId().value(),
                                    restaurant.getName().value(),
                                    restaurant.getCuisine().name(),
                                    new AddressDto(
                                            restaurant.getAddress().street(),
                                            restaurant.getAddress().number(),
                                            restaurant.getAddress().postalCode(),
                                            restaurant.getAddress().city(),
                                            restaurant.getAddress().country()
                                    ),
                                    restaurant.getContactEmail().value(),
                                    restaurant.getImageUrls().stream()
                                            .map(imageUrl -> imageUrl.value())
                                            .toList(),
                                    restaurant.getDefaultPrepTime().minutes(),
                                    new OpeningHoursDto(restaurant.getOpeningHours().toString()),
                                    restaurant.isOpen(),
                                    dishCount
                            );
                        })
                        .toList();

        return ResponseEntity.ok(restaurantDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> findById(@PathVariable UUID id) {
        return getRestaurantByIdUseCase.getRestaurantById(new RestaurantId(id))
                .map(restaurant -> {
                    int dishCount = loadMenuPort.loadByRestaurantId(restaurant.getId())
                            .map(menu -> menu.getDishes().size())
                            .orElse(0);

                    return new RestaurantDto(
                            restaurant.getId().value(),
                            restaurant.getName().value(),
                            restaurant.getCuisine().name(),
                            new AddressDto(
                                    restaurant.getAddress().street(),
                                    restaurant.getAddress().number(),
                                    restaurant.getAddress().postalCode(),
                                    restaurant.getAddress().city(),
                                    restaurant.getAddress().country()
                            ),
                            restaurant.getContactEmail().value(),
                            restaurant.getImageUrls().stream()
                                    .map(imageUrl -> imageUrl.value())
                                    .toList(),
                            restaurant.getDefaultPrepTime().minutes(),
                            new OpeningHoursDto(restaurant.getOpeningHours().toString()),
                            restaurant.isOpen(),
                            dishCount
                    );
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/open")
    public ResponseEntity<Void> openRestaurant(@PathVariable UUID id) {
        String ownerId = currentUserProvider.getCurrentUserId();
        RestaurantId restaurantId = new RestaurantId(id);

        return getRestaurantByIdUseCase.getRestaurantById(restaurantId)
                .filter(restaurant -> restaurant.getOwnerId().value().toString().equals(ownerId))
                .map(restaurant -> {
                    openRestaurantUseCase.openRestaurant(restaurantId);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.status(403).build());
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Void> closeRestaurant(@PathVariable UUID id) {
        String ownerId = currentUserProvider.getCurrentUserId();
        RestaurantId restaurantId = new RestaurantId(id);

        return getRestaurantByIdUseCase.getRestaurantById(restaurantId)
                .filter(restaurant -> restaurant.getOwnerId().value().toString().equals(ownerId))
                .map(restaurant -> {
                    closeRestaurantUseCase.closeRestaurant(restaurantId);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.status(403).build());
    }
}