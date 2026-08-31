package be.kdg.prog6.order.core;

import be.kdg.prog6.order.domain.PriceRangeCriteria;
import be.kdg.prog6.order.port.in.UpdatePriceRangeCriteriaCommand;
import be.kdg.prog6.order.port.in.UpdatePriceRangeCriteriaUseCase;
import be.kdg.prog6.order.port.out.SavePriceRangeCriteriaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UpdatePriceRangeCriteriaUseCaseImpl implements UpdatePriceRangeCriteriaUseCase {

    private final List<SavePriceRangeCriteriaPort> savePriceRangeCriteriaPort;

    public UpdatePriceRangeCriteriaUseCaseImpl(List<SavePriceRangeCriteriaPort> savePriceRangeCriteriaPort) {
        this.savePriceRangeCriteriaPort = savePriceRangeCriteriaPort;
    }

    @Override
    public void updateCriteria(UpdatePriceRangeCriteriaCommand command) {
        PriceRangeCriteria criteria = new PriceRangeCriteria(
                command.lowThreshold(),
                command.mediumThreshold(),
                command.highThreshold(),
                command.effectiveDate()
        );

        savePriceRangeCriteriaPort.forEach(port -> port.save(criteria));
    }
}