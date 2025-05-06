package com.afterpay.frauddetector.mapper;

import java.io.Serializable;

import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.dto.creditcard.FraudDetailsDTO;
import org.mapstruct.Mapper;

@Mapper
public interface FraudDetailsMapper extends BaseMapper<CreditCardTransactionDTO, FraudDetailsDTO>, Serializable {

}
