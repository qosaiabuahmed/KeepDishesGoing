package be.kdg.prog6.order.adapter.in.scheduler;

import be.kdg.prog6.order.port.in.AutoDeclineOrdersUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderScheduler {

    private static final Logger log = LoggerFactory.getLogger(OrderScheduler.class);

    private final AutoDeclineOrdersUseCase autoDeclineOrdersUseCase;

    public OrderScheduler(AutoDeclineOrdersUseCase autoDeclineOrdersUseCase) {
        this.autoDeclineOrdersUseCase = autoDeclineOrdersUseCase;
    }

    @Scheduled(fixedRate = 60000)
    public void autoDeclineExpiredOrders() {
        log.debug("Running scheduled auto-decline check for expired orders");
        autoDeclineOrdersUseCase.autoDeclineExpiredOrders();
    }
}