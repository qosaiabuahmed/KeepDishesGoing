package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.order.port.out.PaymentPort;
import be.kdg.prog6.order.port.out.PaymentResult;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StripePaymentAdapter implements PaymentPort {

    private static final Logger log = LoggerFactory.getLogger(StripePaymentAdapter.class);

    public StripePaymentAdapter(@Value("${stripe.api-key}") String apiKey) {
        Stripe.apiKey = apiKey;
        log.info("Stripe payment adapter initialized");
    }

    @Override
    public PaymentResult processPayment(String customerId, BigDecimal amount, String currency) {
        try {
            long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValue();

            log.info("Processing payment for customer {} - Amount: {} {}",
                    customerId, amount, currency.toUpperCase());

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(currency.toLowerCase())
                    .setDescription("Keep Dishes Going - Order payment")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                    .build()
                    )
                    .setConfirm(true)
                    .setPaymentMethod("pm_card_visa")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            log.info("Payment successful - Transaction ID: {}", intent.getId());

            return PaymentResult.success(intent.getId());

        } catch (StripeException e) {
            log.error("Payment failed for customer {}: {}", customerId, e.getMessage());
            return PaymentResult.failure("Payment failed: " + e.getMessage());
        }
    }
}