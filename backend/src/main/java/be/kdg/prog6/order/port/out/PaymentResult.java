package be.kdg.prog6.order.port.out;

public record PaymentResult(
        String transactionId,
        boolean success,
        String message
) {
    public static PaymentResult success(String transactionId) {
        return new PaymentResult(transactionId, true, "Payment processed successfully");
    }

    public static PaymentResult failure(String message) {
        return new PaymentResult(null, false, message);
    }
}