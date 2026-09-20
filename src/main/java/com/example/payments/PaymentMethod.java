package com.example.payments;

/**
 * Base class for payment methods. Each payment method should have its own transaction fee.
 */
public abstract class PaymentMethod {
    private static double transactionFee = 0.0;

    /**
     * Processes a payment for the supplied amount.
     */
    public abstract PaymentReceipt processPayment(double amount);

    /**
     * Returns the configured transaction fee.
     */
    public double getTransactionFee() {
        return transactionFee;
    }

    /**
     * Sets the transaction fee for this payment method.
     */
    public void setTransactionFee(double fee) {
        transactionFee = fee;
    }
}
