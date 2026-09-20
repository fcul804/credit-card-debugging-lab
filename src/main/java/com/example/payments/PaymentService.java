package com.example.payments;

/**
 * Coordinates payment processing and basic fraud checks.
 */
public class PaymentService {
    /**
     * Processes a payment after checking that the request is not suspicious.
     */
    public PaymentReceipt checkout(PaymentMethod method, double amount, String country) {
        if (isSuspicious(amount, country)) return new PaymentReceipt("UNKNOWN", amount, 0, amount, "BLOCKED_FRAUD");
        return method.processPayment(amount);
    }

    /**
     * Flags large overseas payments for this simplified lab.
     */
    private boolean isSuspicious(double amount, String country) {
        return amount >= 1000 && country == "OVERSEAS";
    }
}
