package com.example.payments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Failing tests that provide entry points for the debugging exercises.
 */
class PaymentDebuggingTest {
    @Test
    void eachPaymentMethodShouldKeepItsOwnFee() {
        PaymentMethod card = new CreditCardPayment(validCard(500));
        PaymentMethod paypal = new PayPalPayment("grad@example.com");
        card.setTransactionFee(2.50);
        paypal.setTransactionFee(1.80);
        assertEquals(2.50, card.getTransactionFee());
        assertEquals(1.80, paypal.getTransactionFee());
    }

    @Test
    void paymentShouldBeDeclinedWhenTotalExceedsCardLimit() {
        CreditCardPayment card = new CreditCardPayment(validCard(101));
        card.setTransactionFee(2.50);
        PaymentReceipt receipt = card.processPayment(100);
        assertEquals("DECLINED_LIMIT", receipt.status());
    }

    @Test
    void suspiciousOverseasPaymentShouldBeBlocked() {
        CreditCardPayment card = new CreditCardPayment(validCard(5000));
        card.setTransactionFee(2.50);
        PaymentReceipt receipt = new PaymentService().checkout(card, 1500, new String("OVERSEAS"));
        assertEquals("BLOCKED_FRAUD", receipt.status());
    }

    @Test
    void invalidCardShouldShowWhereExceptionOriginated() {
        CreditCardPayment card = new CreditCardPayment(new CreditCard("1234", "Alex", 13, 2028, 500));
        assertThrows(IllegalArgumentException.class, () -> card.processPayment(50));
    }

    @Test
    void shouldAddBothApprovedPayments() throws InterruptedException {

        PaymentService service = new PaymentService();

        PaymentMethod card1 =
                new CreditCardPayment(validCard(5000));

        PaymentMethod card2 =
                new CreditCardPayment(validCard(200));

        PaymentBatchProcessor processor = new PaymentBatchProcessor();

        double result =
                processor.processTwoPayments(
                        service,
                        card1,
                        card2,
                        100.00
                );

        assertEquals(
                200.00,
                result,
                "Both approved payments should be included in the total"
        );
    }

    private CreditCard validCard(double limit) {
        return new CreditCard("4111111111111111", "Alex Lee", 12, 2028, limit);
    }
}
