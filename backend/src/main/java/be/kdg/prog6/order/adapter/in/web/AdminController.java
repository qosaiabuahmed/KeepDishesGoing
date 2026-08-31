package be.kdg.prog6.order.adapter.in.web;

import be.kdg.prog6.order.adapter.in.dto.PriceRangeCriteriaDto;
import be.kdg.prog6.order.adapter.in.dto.PriceRangeEvolutionResponse;
import be.kdg.prog6.order.adapter.in.request.UpdatePriceRangeCriteriaRequest;
import be.kdg.prog6.order.domain.PriceRangeCriteria;
import be.kdg.prog6.order.port.in.GetPriceRangeCriteriaUseCase;
import be.kdg.prog6.order.port.in.GetPriceRangeEvolutionUseCase;
import be.kdg.prog6.order.port.in.UpdatePriceRangeCriteriaCommand;
import be.kdg.prog6.order.port.in.UpdatePriceRangeCriteriaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/price-range-criteria")
public class AdminController {

    private final GetPriceRangeCriteriaUseCase getPriceRangeCriteriaUseCase;
    private final UpdatePriceRangeCriteriaUseCase updatePriceRangeCriteriaUseCase;
    private final GetPriceRangeEvolutionUseCase getPriceRangeEvolutionUseCase;

    public AdminController(
            GetPriceRangeCriteriaUseCase getPriceRangeCriteriaUseCase,
            UpdatePriceRangeCriteriaUseCase updatePriceRangeCriteriaUseCase,
            GetPriceRangeEvolutionUseCase getPriceRangeEvolutionUseCase) {
        this.getPriceRangeCriteriaUseCase = getPriceRangeCriteriaUseCase;
        this.updatePriceRangeCriteriaUseCase = updatePriceRangeCriteriaUseCase;
        this.getPriceRangeEvolutionUseCase = getPriceRangeEvolutionUseCase;
    }

    @GetMapping
    public ResponseEntity<PriceRangeCriteriaDto> getCurrentCriteria() {
        PriceRangeCriteria criteria = getPriceRangeCriteriaUseCase.getCurrentCriteria();

        PriceRangeCriteriaDto dto = new PriceRangeCriteriaDto(
                criteria.getLowThreshold(),
                criteria.getMediumThreshold(),
                criteria.getHighThreshold()
        );

        return ResponseEntity.ok(dto);
    }

    @PatchMapping
    public ResponseEntity<PriceRangeCriteriaDto> updateCriteria(@RequestBody UpdatePriceRangeCriteriaRequest request) {
        UpdatePriceRangeCriteriaCommand command = new UpdatePriceRangeCriteriaCommand(
                request.lowThreshold(),
                request.mediumThreshold(),
                request.highThreshold(),
                LocalDateTime.now()
        );

        updatePriceRangeCriteriaUseCase.updateCriteria(command);

        PriceRangeCriteria updated = getPriceRangeCriteriaUseCase.getCurrentCriteria();
        PriceRangeCriteriaDto dto = new PriceRangeCriteriaDto(
                updated.getLowThreshold(),
                updated.getMediumThreshold(),
                updated.getHighThreshold()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/restaurants/{restaurantId}/price-evolution")
    public ResponseEntity<List<PriceRangeEvolutionResponse>> getPriceEvolution(
            @PathVariable UUID restaurantId) {

        List<GetPriceRangeEvolutionUseCase.PriceRangeSnapshot> evolution =
                getPriceRangeEvolutionUseCase.getEvolution(restaurantId);

        List<PriceRangeEvolutionResponse> response = evolution.stream()
                .map(snapshot -> new PriceRangeEvolutionResponse(
                        snapshot.date(),
                        snapshot.priceRange(),
                        snapshot.averageMenuPrice(),
                        snapshot.lowThreshold(),
                        snapshot.mediumThreshold(),
                        snapshot.highThreshold()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }
}