package com.afterpay.frauddetector.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Business object to store transaction data
 *
 * @author shuchi
 */
public class TransactionModel {
    private LocalDateTime timestamp;
    private BigDecimal amount;

    public TransactionModel(final LocalDateTime timestamp, final BigDecimal amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }
}
