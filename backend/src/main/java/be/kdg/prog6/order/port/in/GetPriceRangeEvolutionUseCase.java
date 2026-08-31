package be.kdg.prog6.order.port.in;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetPriceRangeEvolutionUseCase {
    List<PriceRangeSnapshot> getEvolution(UUID restaurantId);

    record PriceRangeSnapshot(
            LocalDateTime date,
            String priceRange,
            BigDecimal averageMenuPrice,
            BigDecimal lowThreshold,
            BigDecimal mediumThreshold,
            BigDecimal highThreshold
    ) {}
}