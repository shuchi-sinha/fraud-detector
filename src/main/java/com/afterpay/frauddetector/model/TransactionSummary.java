package com.afterpay.frauddetector.model;

import java.math.BigDecimal;
import java.util.SortedSet;

/**
 * Business object to store transactions summary
 *
 * @author shuchi
 */
public class TransactionSummary {
    private SortedSet<TransactionModel> transactions;
    private BigDecimal totalAmount;

    public TransactionSummary(final SortedSet<TransactionModel> transactionSet, final BigDecimal threshold) {
        this.transactions = transactionSet;
        this.totalAmount = threshold;
    }

    public SortedSet<TransactionModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(final SortedSet<TransactionModel> transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
