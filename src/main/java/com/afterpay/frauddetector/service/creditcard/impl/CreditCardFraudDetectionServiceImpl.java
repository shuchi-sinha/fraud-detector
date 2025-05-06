package com.afterpay.frauddetector.service.creditcard.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;
import java.util.SortedSet;

import com.afterpay.frauddetector.config.FraudConfig;
import com.afterpay.frauddetector.constants.Constants;
import com.afterpay.frauddetector.dao.impl.CreditCardTransactionDaoImpl;
import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.dto.creditcard.FraudDetailsDTO;
import com.afterpay.frauddetector.mapper.FraudDetailsMapper;
import com.afterpay.frauddetector.model.TransactionModel;
import com.afterpay.frauddetector.model.TransactionSummary;
import com.afterpay.frauddetector.service.FraudDetectionService;
import com.afterpay.frauddetector.util.DateTimeUtil;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author shuchi
 */
@Service("fraudDetectionService")
public class CreditCardFraudDetectionServiceImpl implements FraudDetectionService<CreditCardTransactionDTO>, Serializable {

    private static final long serialVersionUID = 13232323L;

    private final FraudConfig fraudConfig;

    private final CreditCardTransactionDaoImpl creditCardTransactionDao;

    private final FraudDetailsMapper fraudDetailsMapper;

    @Autowired
    public CreditCardFraudDetectionServiceImpl(final CreditCardTransactionDaoImpl cacheService, final FraudConfig config, final FraudDetailsMapper fraudDetailsMapper) {
        this.creditCardTransactionDao = cacheService;
        this.fraudConfig = config;
        this.fraudDetailsMapper = fraudDetailsMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FraudDetailsDTO> processTransaction(final CreditCardTransactionDTO currentCCTransaction) {
        Validate.notNull(currentCCTransaction);
        Validate.notNull(currentCCTransaction.getHashedCreditCard());
        Validate.notNull(currentCCTransaction.getAmount());
        Validate.notNull(currentCCTransaction.getTimestamp());

        Optional<FraudDetailsDTO> fraudulentData = Optional.empty();
        final Optional<TransactionSummary> previousTransactionSummary = creditCardTransactionDao.getTransactionSummary(currentCCTransaction.getHashedCreditCard());
        if (previousTransactionSummary.isPresent() && !CollectionUtils.isEmpty(previousTransactionSummary.get().getTransactions()) && isFraudTransaction(currentCCTransaction, previousTransactionSummary.get())) {
            fraudulentData = Optional.of(fraudDetailsMapper.mapSourceToTarget(currentCCTransaction));
        }
        creditCardTransactionDao.insertTransaction(currentCCTransaction);

        return fraudulentData;
    }

    /**
     * Method to check if the current credit card transaction is fraud
     *
     * @param currentCCTransaction       the credit card transaction record being processed
     * @param previousTransactionSummary the previous transactions summary details
     * @return if the current transaction is fraud or not
     */
    private boolean isFraudTransaction(final CreditCardTransactionDTO currentCCTransaction, final TransactionSummary previousTransactionSummary) {
        removeOutdatedTransactions(currentCCTransaction, previousTransactionSummary);
        final BigDecimal newTransactionsAmount = previousTransactionSummary.getTotalAmount().add(currentCCTransaction.getAmount());
        return newTransactionsAmount.compareTo(fraudConfig.getAmount()) > 0;
    }

    /**
     * Method to remove the outdated transactions which are out of the fraud window from the previous
     * transaction list captured. This will ensure only the transactions relevant to the fraud window
     * are stored and used for the fraud detection for efficient memory usage and performance.
     *
     * @param currentCCTransaction       the credit card transaction record being processed
     * @param previousTransactionSummary the previous transactions summary details
     */
    private void removeOutdatedTransactions(final CreditCardTransactionDTO currentCCTransaction, final TransactionSummary previousTransactionSummary) {
        final SortedSet<TransactionModel> transactions = previousTransactionSummary.getTransactions();
        BigDecimal amountToBeReduced = BigDecimal.valueOf(Constants.ZERO);
        for (final Iterator<TransactionModel> iterator = transactions.iterator(); iterator.hasNext(); ) {
            final TransactionModel transaction = iterator.next();
            if (isOutdatedTransaction(transaction, currentCCTransaction)) {
                amountToBeReduced = amountToBeReduced.add(transaction.getAmount());
                iterator.remove();
            } else {
                break;
            }
        }
        if (amountToBeReduced.compareTo(BigDecimal.valueOf(Constants.ZERO)) != 0) {
            updateTransactionSummary(currentCCTransaction, transactions, previousTransactionSummary.getTotalAmount().subtract(amountToBeReduced));
        }
    }

    /**
     * Method to check if the transaction is outdated i.e. out of the fraud window in comparison to
     * current transaction record
     *
     * @param transactionToBeChecked the transaction record to be checked
     * @param currentCCTransaction   the credit card transaction record being processed
     * @return if the transaction is outdated i.e. out of the fraud window or not
     */
    private boolean isOutdatedTransaction(final TransactionModel transactionToBeChecked, final CreditCardTransactionDTO currentCCTransaction) {
        final long difference = DateTimeUtil.getTimeDifference(fraudConfig.getChronoUnit(), transactionToBeChecked.getTimestamp(), currentCCTransaction.getTimestamp());
        return difference > fraudConfig.getWindow();
    }

    /**
     * Method to update the transaction summary for the new credit card record
     *
     * @param creditCardTransaction the credit card transaction being processed
     * @param transactions          the previous transactions
     * @param totalAmount           the updated total amount
     */
    private void updateTransactionSummary(final CreditCardTransactionDTO creditCardTransaction, final SortedSet<TransactionModel> transactions, final BigDecimal totalAmount) {
        final TransactionSummary updatedTransactionsData = new TransactionSummary(transactions, totalAmount);
        creditCardTransactionDao.updateTransactionSummary(creditCardTransaction.getHashedCreditCard(), updatedTransactionsData);
    }
}
