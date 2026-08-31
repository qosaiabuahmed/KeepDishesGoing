package be.kdg.prog6.order.adapter.in.web;

import be.kdg.prog6.order.adapter.in.dto.BasketDto;
import be.kdg.prog6.order.adapter.in.dto.BasketItemDto;
import be.kdg.prog6.order.adapter.in.dto.ValidationIssueDto;
import be.kdg.prog6.order.adapter.in.dto.ValidationResultDto;
import be.kdg.prog6.order.adapter.in.request.AddDishToBasketRequest;
import be.kdg.prog6.order.domain.Basket;
import be.kdg.prog6.order.domain.BasketItem;
import be.kdg.prog6.order.domain.BasketValidationResult;
import be.kdg.prog6.order.port.in.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/basket")
public class CustomerBasketController {

    private final AddDishToBasketUseCase addDishToBasketUseCase;
    private final RemoveDishFromBasketUseCase removeDishFromBasketUseCase;
    private final GetBasketQuery getBasketQuery;
    private final ClearBasketUseCase clearBasketUseCase;
    private final ValidateBasketQuery validateBasketQuery;

    public CustomerBasketController(
            AddDishToBasketUseCase addDishToBasketUseCase,
            RemoveDishFromBasketUseCase removeDishFromBasketUseCase,
            GetBasketQuery getBasketQuery,
            ClearBasketUseCase clearBasketUseCase,
            ValidateBasketQuery validateBasketQuery) {
        this.addDishToBasketUseCase = addDishToBasketUseCase;
        this.removeDishFromBasketUseCase = removeDishFromBasketUseCase;
        this.getBasketQuery = getBasketQuery;
        this.clearBasketUseCase = clearBasketUseCase;
        this.validateBasketQuery = validateBasketQuery;
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addDish(
            @RequestHeader("X-Customer-Id") String customerId,
            @RequestBody AddDishToBasketRequest request) {

        AddDishToBasketCommand command = new AddDishToBasketCommand(
                customerId,
                request.restaurantId(),
                request.dishId(),
                request.dishName(),
                request.unitPrice(),
                request.quantity()
        );

        addDishToBasketUseCase.addDish(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{dishId}")
    public ResponseEntity<Void> removeDish(
            @RequestHeader("X-Customer-Id") String customerId,
            @PathVariable String dishId) {

        RemoveDishFromBasketCommand command = new RemoveDishFromBasketCommand(
                customerId,
                dishId
        );

        removeDishFromBasketUseCase.removeDish(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<BasketDto> getBasket(
            @RequestHeader("X-Customer-Id") String customerId) {

        Basket basket = getBasketQuery.getBasket(customerId);
        BasketDto dto = toDto(basket);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearBasket(
            @RequestHeader("X-Customer-Id") String customerId) {

        ClearBasketCommand command = new ClearBasketCommand(customerId);
        clearBasketUseCase.clearBasket(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidationResultDto> validateBasket(
            @RequestHeader("X-Customer-Id") String customerId) {

        BasketValidationResult result = validateBasketQuery.validateBasket(customerId);
        ValidationResultDto dto = toDto(result);
        return ResponseEntity.ok(dto);
    }

    private BasketDto toDto(Basket basket) {
        List<BasketItemDto> items = basket.getItems().stream()
                .map(this::toDto)
                .toList();

        return new BasketDto(
                basket.getId().value().toString(),
                basket.getRestaurantId() != null ? basket.getRestaurantId().value().toString() : null,
                items,
                basket.calculateTotal(),
                basket.getTotalItemCount()
        );
    }

    private BasketItemDto toDto(BasketItem item) {
        return new BasketItemDto(
                item.getDishId().value().toString(),
                item.getDishName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }

    private ValidationResultDto toDto(BasketValidationResult result) {
        List<ValidationIssueDto> issueDtos = result.getIssues().stream()
                .map(issue -> new ValidationIssueDto(
                        issue.dishId().value().toString(),
                        issue.dishName(),
                        issue.issueType().name(),
                        issue.message()
                ))
                .toList();

        return new ValidationResultDto(
                result.isValid(),
                issueDtos
        );
    }
}