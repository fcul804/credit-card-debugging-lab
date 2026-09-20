package com.example.payments;

/**
 * Processes payments made using a credit card.
 */
public class CreditCardPayment extends PaymentMethod {

    private final CreditCard card;

    /**
     * Creates a credit-card payment processor for a card.
     */
    public CreditCardPayment(CreditCard card) {
        this.card = card;
    }

    /**
     * Validates the card, calculates the total and returns a receipt.
     */
    @Override
    public PaymentReceipt processPayment(double amount) {
        validateCard();
        double fee = getTransactionFee();
        double total = amount + fee;
        if (card.availableLimit() >= amount) {
            return new PaymentReceipt("CREDIT_CARD", amount, fee, total, "APPROVED");

        }
        return new PaymentReceipt("CREDIT_CARD", amount, fee, total, "DECLINED_LIMIT");
    }

    /**
     * Performs simple validation suitable for this teaching example.
     */
    private void validateCard() {
        if (card.number().length() != 16) throw new IllegalArgumentException("Card number must contain 16 digits");
        if (card.expiryMonth() < 1 || card.expiryMonth() > 12)
            throw new IllegalArgumentException("Invalid expiry month");
    }
}
