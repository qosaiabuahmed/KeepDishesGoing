package be.kdg.prog6.order.core;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.port.in.GetOrderQuery;
import be.kdg.prog6.order.port.in.GetOrderQueryRequest;
import be.kdg.prog6.order.port.out.LoadOrderPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetOrderQueryImpl implements GetOrderQuery {

    private final LoadOrderPort loadOrderPort;

    public GetOrderQueryImpl(LoadOrderPort loadOrderPort) {
        this.loadOrderPort = loadOrderPort;
    }

    @Override
    public Optional<Order> getOrder(GetOrderQueryRequest query) {
        OrderId id = OrderId.of(query.orderId());
        return loadOrderPort.loadById(id);
    }
}