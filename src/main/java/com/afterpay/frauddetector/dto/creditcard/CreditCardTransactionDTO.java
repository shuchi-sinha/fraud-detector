package com.afterpay.frauddetector.dto.creditcard;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.afterpay.frauddetector.dto.TransactionDTO;

/**
 * The credit card transaction data transfer object
 *
 * @author shuchi
 */
public class CreditCardTransactionDTO extends TransactionDTO {

    /**
     *
     */
    private static final long serialVersionUID = 499174547496110287L;

    private String hashedCreditCard;

    public CreditCardTransactionDTO(final String hashedCreditCard, final LocalDateTime timestamp, final BigDecimal amount) {
        super(timestamp, amount);
        this.hashedCreditCard = hashedCreditCard;
    }

    public String getHashedCreditCard() {
        return hashedCreditCard;
    }

    public void setHashedCreditCard(final String hashedCreditCard) {
        this.hashedCreditCard = hashedCreditCard;
    }
}
