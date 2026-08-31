package be.kdg.prog6.order.core;

import be.kdg.prog6.order.domain.PriceRangeCriteria;
import be.kdg.prog6.order.port.in.GetPriceRangeCriteriaUseCase;
import be.kdg.prog6.order.port.out.LoadPriceRangeCriteriaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetPriceRangeCriteriaUseCaseImpl implements GetPriceRangeCriteriaUseCase {

    private final LoadPriceRangeCriteriaPort loadPriceRangeCriteriaPort;

    public GetPriceRangeCriteriaUseCaseImpl(LoadPriceRangeCriteriaPort loadPriceRangeCriteriaPort) {
        this.loadPriceRangeCriteriaPort = loadPriceRangeCriteriaPort;
    }

    @Override
    public PriceRangeCriteria getCurrentCriteria() {
        return loadPriceRangeCriteriaPort.loadCurrent()
                .orElse(PriceRangeCriteria.getDefault());
    }
}