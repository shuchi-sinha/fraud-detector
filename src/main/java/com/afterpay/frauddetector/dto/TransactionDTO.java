package com.afterpay.frauddetector.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The transaction data transfer object
 *
 * @author shuchi
 */
public class TransactionDTO implements Serializable {

    private static final long serialVersionUID = -2420731471948817358L;

    private LocalDateTime timestamp;
    private BigDecimal amount;

    public TransactionDTO(final LocalDateTime timestamp, final BigDecimal amount) {
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
