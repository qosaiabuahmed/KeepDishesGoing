package be.kdg.prog6.order.adapter.in.web;

import be.kdg.prog6.order.domain.RestaurantProjection;
import be.kdg.prog6.order.port.in.GetRestaurantsForCustomerQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/restaurants")
public class CustomerRestaurantController {

    private final GetRestaurantsForCustomerQuery getRestaurantsQuery;

    public CustomerRestaurantController(GetRestaurantsForCustomerQuery getRestaurantsQuery) {
        this.getRestaurantsQuery = getRestaurantsQuery;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants(
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String priceRange
    ) {
        List<RestaurantProjection> projections = getRestaurantsQuery.getAllRestaurants();

        List<RestaurantDto> dtos = projections.stream()
                .filter(p -> cuisine == null || p.getCuisine().equalsIgnoreCase(cuisine))
                .filter(p -> priceRange == null || (p.getPriceRange() != null && p.getPriceRange().equalsIgnoreCase(priceRange)))
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    private RestaurantDto toDto(RestaurantProjection projection) {
        return new RestaurantDto(
                projection.getRestaurantId().value().toString(),
                projection.getName(),
                projection.getCuisine(),
                new AddressDto(
                        projection.getStreet(),
                        projection.getNumber(),
                        projection.getPostalCode(),
                        projection.getCity(),
                        projection.getCountry()
                ),
                projection.getContactEmail(),
                projection.getImageUrls(),
                projection.isOpen(),
                projection.getPriceRange(),
                projection.getAverageMenuPrice()
        );
    }

    record RestaurantDto(
            String id,
            String name,
            String cuisine,
            AddressDto address,
            String contactEmail,
            List<String> imageUrls,
            boolean isOpen,
            String priceRange,
            java.math.BigDecimal averageMenuPrice
    ) {}

    record AddressDto(
            String street,
            String number,
            String postalCode,
            String city,
            String country
    ) {}
}