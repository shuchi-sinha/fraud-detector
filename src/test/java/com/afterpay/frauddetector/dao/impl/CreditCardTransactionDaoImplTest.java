package com.afterpay.frauddetector.dao.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.mapper.TransactionMapper;
import com.afterpay.frauddetector.model.TransactionModel;
import com.afterpay.frauddetector.model.TransactionSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditCardTransactionDaoImplTest {

    public static final String CREDIT_CARD_HASH = "hashedCard";

    @Mock
    TransactionMapper transactionMapper;

    @InjectMocks
    CreditCardTransactionDaoImpl creditCardTransactionDao;

    @Test
    void getTransactionSummaryWhenItsFirstTransaction() {
        // given

        // when
        final Optional<TransactionSummary> transactionSummary = creditCardTransactionDao.getTransactionSummary(CREDIT_CARD_HASH);

        // then
        assertTrue(transactionSummary.isPresent());
        assertEquals(transactionSummary.get().getTotalAmount(), BigDecimal.ZERO);
        assertEquals(transactionSummary.get().getTransactions().size(), 0);
    }

    @Test
    void updateTransactionSummary() {
        // given
        final SortedSet<TransactionModel> transactions = new TreeSet<>((a1, a2) -> a1.getTimestamp().compareTo(a2.getTimestamp()));
        transactions.add(new TransactionModel(LocalDateTime.now(), BigDecimal.ONE));
        final TransactionSummary transactionSummary = new TransactionSummary(transactions, BigDecimal.TEN);

        // when
        creditCardTransactionDao.updateTransactionSummary(CREDIT_CARD_HASH, transactionSummary);

        // then
        assertTrue(creditCardTransactionDao.getTransactionSummary(CREDIT_CARD_HASH).isPresent());
        assertEquals(creditCardTransactionDao.getTransactionSummary(CREDIT_CARD_HASH).get().getTransactions().size(), 1);
    }

    @Test
    void insertTransaction() {
        final LocalDateTime now = LocalDateTime.now(); // given
        final CreditCardTransactionDTO creditCardTransaction = new CreditCardTransactionDTO(CREDIT_CARD_HASH, now, BigDecimal.ONE);
        final TransactionModel transactionModel = new TransactionModel(now, BigDecimal.ONE);

        when(transactionMapper.mapSourceToTarget(creditCardTransaction)).thenReturn(transactionModel);

        // when
        creditCardTransactionDao.insertTransaction(creditCardTransaction);

        // then
        assertTrue(creditCardTransactionDao.getTransactionSummary(CREDIT_CARD_HASH).isPresent());

        assertEquals(creditCardTransactionDao.getTransactionSummary(CREDIT_CARD_HASH).get().getTransactions().size(), 1);
    }
}
