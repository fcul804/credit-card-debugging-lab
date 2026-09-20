package com.example.payments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Failing tests that provide entry points for the debugging exercises.
 */
class PaymentDebuggingTest {

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
    void transactionListShouldIncludeOnlyApprovedTotals() {
        List<PaymentReceipt> transactions = List.of(
                new PaymentReceipt("CREDIT_CARD", 40, 2, 42, "APPROVED"),
                new PaymentReceipt("PAYPAL", 25, 1, 26, "APPROVED"),
                new PaymentReceipt("CREDIT_CARD", 200, 2, 202, "DECLINED_LIMIT"),
                new PaymentReceipt("PAYPAL", 10, 1, 11, "APPROVED"),
                new PaymentReceipt("UNKNOWN", 1500, 0, 1500, "BLOCKED_FRAUD"),
                new PaymentReceipt("CREDIT_CARD", 60, 2, 62, "APPROVED")
        );

        double result = new PaymentBatchProcessor().totalApprovedTransactions(transactions);

        assertEquals(141.00, result,
                "Only approved transactions, including their fees, should contribute to the total");
    }

    @Test
    void invalidCardShouldShowWhereExceptionOriginated() {
        CreditCardPayment card = new CreditCardPayment(new CreditCard("1234", "Alex", 13, 2028, 500));
        assertThrows(IllegalArgumentException.class, () -> card.processPayment(50));
    }

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
    void shouldAddBothApprovedPayments() throws InterruptedException {

        PaymentService service = new PaymentService();

        PaymentMethod card1 = new CreditCardPayment(validCard(5000));
        PaymentMethod card2 = new CreditCardPayment(validCard(200));

        PaymentBatchProcessor processor = new PaymentBatchProcessor();

        double result = processor.processTwoPayments(service, card1, card2, 100.00);
        assertEquals(200.00, result, "Both approved payments should be included in the total");
    }

    private CreditCard validCard(double limit) {
        return new CreditCard("4111111111111111", "Alex Lee", 12, 2028, limit);
    }
}
