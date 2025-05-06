package com.afterpay.frauddetector.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.afterpay.frauddetector.constants.Constants;
import com.afterpay.frauddetector.dao.CreditCardTransactionDao;
import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.mapper.TransactionMapper;
import com.afterpay.frauddetector.model.TransactionModel;
import com.afterpay.frauddetector.model.TransactionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Java in-memory based implementation of {@link CreditCardTransactionDao}.
 *
 * <p>Uses two hash maps for the storage of transactions and total amount for every hashed credit
 * card
 *
 * @author shuchi
 */
@Repository("creditCardTransactionDao")
public class CreditCardTransactionDaoImpl implements CreditCardTransactionDao, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardTransactionDaoImpl.class);

    private static final long serialVersionUID = -8447977973532991154L;

    private final Map<String, SortedSet<TransactionModel>> creditCardTransactions;
    
    private final Map<String, BigDecimal> creditCardTotalAmount;

    private final TransactionMapper transactionMapper;

    public CreditCardTransactionDaoImpl(final TransactionMapper transactionMapper) {
        this.creditCardTransactions = new ConcurrentHashMap<>();
        this.creditCardTotalAmount = new ConcurrentHashMap<>();
        this.transactionMapper = transactionMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TransactionSummary> getTransactionSummary(final String hashedCreditCard) {
        TransactionSummary transactionSummary;
        if (creditCardTransactions.containsKey(hashedCreditCard) && creditCardTotalAmount.containsKey(hashedCreditCard)) {
            transactionSummary = new TransactionSummary(new TreeSet<>(creditCardTransactions.get(hashedCreditCard)), creditCardTotalAmount.get(hashedCreditCard));
        } else if (creditCardTransactions.containsKey(hashedCreditCard) || creditCardTotalAmount.containsKey(hashedCreditCard)) {

            // Very rare scenario in case both the cache/maps get out of sync in case of any disaster or
            // application issue.
            LOGGER.error(Constants.CACHE_DATA_IS_NOT_IN_SYNC, hashedCreditCard);
            transactionSummary = null;

        } else {
            transactionSummary = new TransactionSummary(Collections.emptySortedSet(), BigDecimal.valueOf(Constants.ZERO));
        }
        return Optional.ofNullable(transactionSummary);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTransactionSummary(final String hashedCreditCard, final TransactionSummary transactionsData) {
        updateCreditCardTransactions(hashedCreditCard, transactionsData.getTransactions());
        updateCreditCardTotalAmount(hashedCreditCard, transactionsData.getTotalAmount());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTransaction(final CreditCardTransactionDTO creditCardTransaction) {
        insertToCreditCardTransactions(creditCardTransaction.getHashedCreditCard(), creditCardTransaction);
        insertToCreditCardTotalAmount(creditCardTransaction.getHashedCreditCard(), creditCardTransaction);
    }

    /**
     * Method to add new transaction in the credit card transaction map
     *
     * @param hashedCreditCard      the hashed credit card for which insert is required
     * @param creditCardTransaction the new transaction
     */
    private void insertToCreditCardTransactions(final String hashedCreditCard, final CreditCardTransactionDTO creditCardTransaction) {
        final SortedSet<TransactionModel> transactionSet = creditCardTransactions.containsKey(hashedCreditCard) ? creditCardTransactions.get(hashedCreditCard) : new TreeSet<>((t1, t2) -> t1.getTimestamp().compareTo(t2.getTimestamp()));
        transactionSet.add(transactionMapper.mapSourceToTarget(creditCardTransaction));
        updateCreditCardTransactions(hashedCreditCard, transactionSet);
    }

    /**
     * Method to add the new transaction amount to the credit card transaction total map
     *
     * @param hashedCreditCard      the hashed credit card for which insert is required
     * @param creditCardTransaction the new transaction
     */
    private void insertToCreditCardTotalAmount(final String hashedCreditCard, final CreditCardTransactionDTO creditCardTransaction) {
        updateCreditCardTotalAmount(hashedCreditCard, creditCardTotalAmount.getOrDefault(hashedCreditCard, BigDecimal.valueOf(Constants.ZERO)).add(creditCardTransaction.getAmount()));
    }

    /**
     * Method to update the credit card transaction map with the updated transactions
     *
     * @param hashedCreditCard    the hashed credit card for which update is required
     * @param updatedTransactions the updated set of transaction
     */
    private void updateCreditCardTransactions(final String hashedCreditCard, final SortedSet<TransactionModel> updatedTransactions) {
        creditCardTransactions.put(hashedCreditCard, updatedTransactions);
    }

    /**
     * Method to update the credit card transaction total for the provided card
     *
     * @param hashedCreditCard   the hashed credit card for which update is required
     * @param updatedTotalAmount the updated total value
     */
    private void updateCreditCardTotalAmount(final String hashedCreditCard, final BigDecimal updatedTotalAmount) {
        creditCardTotalAmount.put(hashedCreditCard, updatedTotalAmount);
    }
}
