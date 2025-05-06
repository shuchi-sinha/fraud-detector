package com.afterpay.frauddetector.service;

import java.util.Optional;

import com.afterpay.frauddetector.dto.TransactionDTO;
import com.afterpay.frauddetector.dto.creditcard.FraudDetailsDTO;

/**
 * This interface provided methods to detect the fraud transactions
 *
 * @param <T> types of TransactionDTO objects handled by this interface
 * @author shuchi
 */
public interface FraudDetectionService<T extends TransactionDTO> {

    /**
     * This method processes the transaction and returns the optional fraud details
     *
     * @param transaction the transaction record
     * @return Optional of Fraud Data
     */
    public Optional<FraudDetailsDTO> processTransaction(final T transaction);
}
