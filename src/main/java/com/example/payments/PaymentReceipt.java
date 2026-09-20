package com.example.payments;

/**
 * Immutable result returned after a payment is processed.
 */
public record PaymentReceipt(String method, double amount, double fee, double total, String status) {
}
