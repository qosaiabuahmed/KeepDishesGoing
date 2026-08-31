package be.kdg.prog6.order.port.out;

import java.math.BigDecimal;

public interface PaymentPort {
    PaymentResult processPayment(String customerId, BigDecimal amount, String currency);
}