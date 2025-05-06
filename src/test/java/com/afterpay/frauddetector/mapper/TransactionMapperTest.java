package com.afterpay.frauddetector.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.model.TransactionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private static final String CREDIT_CARD_HASH = "creditCardHash";

    @InjectMocks
    TransactionMapper transactionMapper;

    @BeforeEach
    void init() {
        transactionMapper = new TransactionMapperImpl();
    }

    @Test
    void testMapCreditCardTransactionDTOToTransactionModel() {
        //given
        LocalDateTime timestamp = LocalDateTime.now();
        final CreditCardTransactionDTO creditCardTransaction = new CreditCardTransactionDTO(CREDIT_CARD_HASH, timestamp, BigDecimal.ONE);

        //when
        TransactionModel transactionModel = transactionMapper.mapSourceToTarget(creditCardTransaction);

        //then
        assertEquals(transactionModel.getAmount(),BigDecimal.ONE);
        assertEquals(transactionModel.getTimestamp(), timestamp);

    }

}