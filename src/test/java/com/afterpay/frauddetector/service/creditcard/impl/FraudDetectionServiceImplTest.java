package com.afterpay.frauddetector.service.creditcard.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import com.afterpay.frauddetector.config.FraudConfig;
import com.afterpay.frauddetector.dao.impl.CreditCardTransactionDaoImpl;
import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.dto.creditcard.FraudDetailsDTO;
import com.afterpay.frauddetector.mapper.FraudDetailsMapper;
import com.afterpay.frauddetector.model.TransactionModel;
import com.afterpay.frauddetector.model.TransactionSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FraudDetectionServiceImplTest {

    public static final String CREDIT_CARD_HASH = "hashedCard";

    @Mock
    CreditCardTransactionDaoImpl creditCardTransactionDao;

    @Mock
    FraudConfig fraudConfig;

    @Mock
    FraudDetailsMapper fraudDetailsMapper;

    @InjectMocks
    CreditCardFraudDetectionServiceImpl fraudDetectionService;

    @BeforeEach
    public void init() throws IllegalArgumentException {
        // given
        when(fraudConfig.getAmount()).thenReturn(BigDecimal.TEN);
        when(fraudConfig.getChronoUnit()).thenReturn(ChronoUnit.SECONDS);
        when(fraudConfig.getWindow()).thenReturn(100L);
        doNothing().when(creditCardTransactionDao).insertTransaction(any());
    }

    @Test
    void testTransactionsWhenAmountForOneCardIsMoreThanThreshold() {

        // given
        final SortedSet<TransactionModel> transactions = new TreeSet<>((a1, a2) -> a1.getTimestamp().compareTo(a2.getTimestamp()));
        transactions.add(new TransactionModel(LocalDateTime.now(), BigDecimal.ONE));
        final TransactionSummary transactionSummary = new TransactionSummary(transactions, BigDecimal.TEN);
        when(creditCardTransactionDao.getTransactionSummary(any())).thenReturn(Optional.of(transactionSummary));


        final CreditCardTransactionDTO creditCardTransaction = new CreditCardTransactionDTO(CREDIT_CARD_HASH, LocalDateTime.now(), BigDecimal.ONE);
        final FraudDetailsDTO fraudDetails = new FraudDetailsDTO(CREDIT_CARD_HASH);
        when(fraudDetailsMapper.mapSourceToTarget(creditCardTransaction)).thenReturn(fraudDetails);

        // when
        final Optional<FraudDetailsDTO> fraudData = fraudDetectionService.processTransaction(creditCardTransaction);

        // then
        assertEquals(fraudData.isPresent(), true);
    }


    @Test
    void testTransactionsWhenPreviousTransactionIsOutdated() {

        // given
        final SortedSet<TransactionModel> transactions = new TreeSet<>((a1, a2) -> a1.getTimestamp().compareTo(a2.getTimestamp()));
        transactions.add(new TransactionModel(LocalDateTime.now().minusMinutes(2L), BigDecimal.ONE));
        final TransactionSummary transactionSummary = new TransactionSummary(transactions, BigDecimal.TEN);
        when(creditCardTransactionDao.getTransactionSummary(any())).thenReturn(Optional.of(transactionSummary));


        final CreditCardTransactionDTO creditCardTransaction = new CreditCardTransactionDTO(CREDIT_CARD_HASH, LocalDateTime.now(), BigDecimal.ONE);
        final FraudDetailsDTO fraudDetails = new FraudDetailsDTO(CREDIT_CARD_HASH);
        when(fraudDetailsMapper.mapSourceToTarget(creditCardTransaction)).thenReturn(fraudDetails);

        // when
        final Optional<FraudDetailsDTO> fraudData = fraudDetectionService.processTransaction(creditCardTransaction);

        // then
        assertEquals(fraudData.isPresent(), true);
    }

    @Test
    void testTransactionsWhenAmountForOneCardIsLessThanThreshold() {

        // given
        final SortedSet<TransactionModel> transactions = new TreeSet<>((a1, a2) -> a1.getTimestamp().compareTo(a2.getTimestamp()));
        transactions.add(new TransactionModel(LocalDateTime.now(), BigDecimal.ONE));
        final TransactionSummary transactionSummary = new TransactionSummary(transactions, BigDecimal.ONE);
        when(creditCardTransactionDao.getTransactionSummary(any())).thenReturn(Optional.of(transactionSummary));

        // when
        final CreditCardTransactionDTO creditCardTransaction = new CreditCardTransactionDTO(CREDIT_CARD_HASH, LocalDateTime.now(), BigDecimal.ONE);
        final Optional<FraudDetailsDTO> fraudData = fraudDetectionService.processTransaction(creditCardTransaction);

        // then
        assertEquals(fraudData.isPresent(), false);
    }
}
