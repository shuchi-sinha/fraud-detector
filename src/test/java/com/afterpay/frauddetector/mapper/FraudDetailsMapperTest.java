package com.afterpay.frauddetector.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.dto.creditcard.FraudDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FraudDetailsMapperTest {

    private static final String CREDIT_CARD_HASH = "creditCardHash";

    FraudDetailsMapper fraudDetailsMapper;

    @BeforeEach
    void init() {
        fraudDetailsMapper = new FraudDetailsMapperImpl();
    }

    @Test
    void testMapCreditCardTransactionDTOToFraudDetailsDTO() {
        //given
        LocalDateTime timestamp = LocalDateTime.now();
        final CreditCardTransactionDTO creditCardTransaction = new CreditCardTransactionDTO(CREDIT_CARD_HASH, timestamp, BigDecimal.ONE);

        //when
        FraudDetailsDTO fraudDetailsDTO = fraudDetailsMapper.mapSourceToTarget(creditCardTransaction);

        //then
        assertEquals(fraudDetailsDTO.getHashedCreditCard(), CREDIT_CARD_HASH);

    }

}