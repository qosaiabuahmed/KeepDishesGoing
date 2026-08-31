package be.kdg.prog6.order.port.out;

import be.kdg.prog6.order.domain.PriceRangeCriteria;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LoadPriceRangeCriteriaPort {
    Optional<PriceRangeCriteria> loadCurrent();
    Optional<PriceRangeCriteria> loadAtDate(LocalDateTime date);
}