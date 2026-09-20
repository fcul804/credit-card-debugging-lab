package com.example.payments;

/**
 * Minimal credit-card data used by the debugging exercises.
 */
public record CreditCard(String number, String holderName, int expiryMonth, int expiryYear, double availableLimit) {
}
