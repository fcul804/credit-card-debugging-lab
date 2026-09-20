package com.example.payments;

/**
 * Processes payments concurrently to demonstrate debugger thread inspection.
 */
public class PaymentBatchProcessor {
    private double approvedTotal = 0;

    /**
     * Processes two payments on named worker threads and returns the approved total.
     */
    public double processTwoPayments(PaymentService service, PaymentMethod first, PaymentMethod second, double amount) throws InterruptedException {
        approvedTotal = 0;
        Thread t1 = new Thread(() -> addIfApproved(service.checkout(first, amount, "NZ")), "payment-worker-1");
        Thread t2 = new Thread(() -> addIfApproved(service.checkout(second, amount, "NZ")), "payment-worker-2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        return approvedTotal;
    }

    /**
     * Adds an approved receipt to the running total. Intentionally unsafe for the lab.
     */
    private void addIfApproved(PaymentReceipt receipt) {
        if (receipt.status().equals("APPROVED")) approvedTotal += receipt.total(); // BUG 4: race condition
    }
}
