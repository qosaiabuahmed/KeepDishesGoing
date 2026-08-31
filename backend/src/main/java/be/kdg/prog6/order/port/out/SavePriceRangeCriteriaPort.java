package be.kdg.prog6.order.port.out;

import be.kdg.prog6.order.domain.PriceRangeCriteria;

public interface SavePriceRangeCriteriaPort {
    void save(PriceRangeCriteria criteria);
}