package be.kdg.prog6.order.port.out;

import be.kdg.prog6.order.domain.PriceRangeCriteria;

import java.util.List;

public interface LoadPriceRangeCriteriaHistoryPort {
    List<PriceRangeCriteria> loadAllHistory();
}