package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.PriceRangeCriteria;
import be.kdg.prog6.order.domain.RestaurantProjection;
import be.kdg.prog6.order.port.in.GetPriceRangeEvolutionUseCase;
import be.kdg.prog6.order.port.out.LoadPriceRangeCriteriaHistoryPort;
import be.kdg.prog6.order.port.out.LoadRestaurantProjectionPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GetPriceRangeEvolutionUseCaseImpl implements GetPriceRangeEvolutionUseCase {

    private final LoadPriceRangeCriteriaHistoryPort criteriaHistoryPort;
    private final LoadRestaurantProjectionPort restaurantProjectionPort;

    public GetPriceRangeEvolutionUseCaseImpl(
            LoadPriceRangeCriteriaHistoryPort criteriaHistoryPort,
            LoadRestaurantProjectionPort restaurantProjectionPort) {
        this.criteriaHistoryPort = criteriaHistoryPort;
        this.restaurantProjectionPort = restaurantProjectionPort;
    }

    @Override
    public List<PriceRangeSnapshot> getEvolution(UUID restaurantId) {
        RestaurantProjection restaurant = restaurantProjectionPort
                .loadById(new RestaurantId(restaurantId))
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));

        BigDecimal avgPrice = restaurant.getAverageMenuPriceOrZero();

        List<PriceRangeCriteria> history = criteriaHistoryPort.loadAllHistory();

        return history.stream()
                .map(criteria -> new PriceRangeSnapshot(
                        criteria.getEffectiveDate(),
                        criteria.calculatePriceRange(avgPrice),
                        avgPrice,
                        criteria.getLowThreshold(),
                        criteria.getMediumThreshold(),
                        criteria.getHighThreshold()
                ))
                .toList();
    }
}