package be.kdg.prog6.order.core;

import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.domain.OrderStatus;
import be.kdg.prog6.order.port.in.AutoDeclineOrdersUseCase;
import be.kdg.prog6.order.port.out.LoadOrderPort;
import be.kdg.prog6.order.port.out.SaveOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AutoDeclineOrdersUseCaseImpl implements AutoDeclineOrdersUseCase {

    private static final Logger log = LoggerFactory.getLogger(AutoDeclineOrdersUseCaseImpl.class);

    private final LoadOrderPort loadOrderPort;
    private final List<SaveOrderPort> saveOrderPorts;

    public AutoDeclineOrdersUseCaseImpl(
            LoadOrderPort loadOrderPort,
            List<SaveOrderPort> saveOrderPorts) {
        this.loadOrderPort = loadOrderPort;
        this.saveOrderPorts = saveOrderPorts;
    }

    @Override
    @Transactional
    public void autoDeclineExpiredOrders() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime threshold = currentTime.minusMinutes(Order.AUTO_DECLINE_MINUTES);

        List<Order> expiredOrders = loadOrderPort.findByStatusAndOrderTimeBefore(
                OrderStatus.SUBMITTED,
                threshold
        );

        log.info("Checking for expired orders. Found {} orders to auto-decline", expiredOrders.size());

        for (Order order : expiredOrders) {
            log.info("Auto-declining order: {}", order.getOrderId().value());

            order.autoDecline(Order.AUTO_DECLINE_REASON);

            saveOrderPorts.forEach(port -> port.save(order));

            log.info("Auto-declined order: {}", order.getOrderId().value());
        }

        log.info("Auto-decline process completed. Declined {} orders", expiredOrders.size());
    }
}