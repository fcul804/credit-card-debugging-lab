package com.example.payments;

/**
 * Simple second payment method used to expose shared-state bugs.
 */
public class PayPalPayment extends PaymentMethod {
    private final String email;

    /**
     * Creates a PayPal payment processor for an account email.
     */
    public PayPalPayment(String email) {
        this.email = email;
    }

    /**
     * Processes the payment and returns a receipt.
     */
    @Override
    public PaymentReceipt processPayment(double amount) {
        if (!email.contains("@")) throw new IllegalArgumentException("Invalid email");
        double fee = getTransactionFee();
        return new PaymentReceipt("PAYPAL", amount, fee, amount + fee, "APPROVED");
    }
}
