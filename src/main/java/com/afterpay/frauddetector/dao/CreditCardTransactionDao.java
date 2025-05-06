package com.afterpay.frauddetector.dao;

import java.util.Optional;

import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.model.TransactionSummary;

/**
 * Interface to perform crud operations on the transaction data
 *
 * @author shuchi
 */
public interface CreditCardTransactionDao {

    /**
     * Method to get the transaction summary for the provided hashed credit card
     *
     * @param hashedCreditCard the hashed credit card value
     * @return the optional of transaction summary
     */
    Optional<TransactionSummary> getTransactionSummary(final String hashedCreditCard);

    /**
     * Method to update the transaction summary for the provided hashed credit card
     *
     * @param hashedCreditCard   the hashed credit card value
     * @param transactionSummary the transaction summary
     */
    void updateTransactionSummary(final String hashedCreditCard, final TransactionSummary transactionSummary);

    /**
     * Method to insert the credit card transaction in the transaction summary
     *
     * @param creditCardTransaction the credit card transaction to be added
     */
    void insertTransaction(final CreditCardTransactionDTO creditCardTransaction);
}
