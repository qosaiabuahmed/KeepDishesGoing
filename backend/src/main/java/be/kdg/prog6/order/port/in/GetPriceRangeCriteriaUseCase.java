package be.kdg.prog6.order.port.in;

import be.kdg.prog6.order.domain.PriceRangeCriteria;

public interface GetPriceRangeCriteriaUseCase {
    PriceRangeCriteria getCurrentCriteria();
}