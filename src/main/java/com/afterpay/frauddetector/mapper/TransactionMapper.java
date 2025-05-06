package com.afterpay.frauddetector.mapper;

import java.io.Serializable;

import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.model.TransactionModel;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper extends BaseMapper<CreditCardTransactionDTO, TransactionModel>, Serializable {

}
