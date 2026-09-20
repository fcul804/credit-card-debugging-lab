package com.example.payments;

/**
 * Small runnable example for exploring the payment code outside the tests.
 */
public class Main {
    /**
     * Configures two payment methods and prints their results.
     */
    public static void main(String[] args) {
        CreditCard card = new CreditCard("4111111111111111", "Alex Lee", 13, 2028, 500);
        CreditCardPayment creditCard = new CreditCardPayment(card);
        PayPalPayment paypal = new PayPalPayment("alex@example.com");
        creditCard.setTransactionFee(2.50);
        paypal.setTransactionFee(1.80);
        PaymentService service = new PaymentService();
        System.out.println(service.checkout(creditCard, 100, "NZ"));
        System.out.println(service.checkout(paypal, 100, "NZ"));
    }
}
