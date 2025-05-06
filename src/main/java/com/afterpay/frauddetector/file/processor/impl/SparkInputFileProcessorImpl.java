package com.afterpay.frauddetector.file.processor.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.afterpay.frauddetector.constants.Constants;
import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.dto.creditcard.FraudDetailsDTO;
import com.afterpay.frauddetector.exception.AfterPayBusinessException;
import com.afterpay.frauddetector.file.processor.InputFileProcessor;
import com.afterpay.frauddetector.handler.SparkContextHandler;
import com.afterpay.frauddetector.service.FraudDetectionService;
import com.afterpay.frauddetector.util.DateTimeUtil;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

/**
 * Spark based implementation of the {@link InputFileProcessor} to process the input file in
 * parallel. Creates workers based on partitioned data and checks the transactions for fraud.
 *
 * @author shuchi
 */
@Service("creditCardFileProcessor")
public class SparkInputFileProcessorImpl implements InputFileProcessor, Serializable {

    private static final long serialVersionUID = 2961223419974009705L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkInputFileProcessorImpl.class);

    private final transient SparkContextHandler contextHandler;
    
    private final FraudDetectionService<CreditCardTransactionDTO> fraudDetectionService;

    @Autowired
    public SparkInputFileProcessorImpl(final SparkContextHandler contextHandler, final FraudDetectionService<CreditCardTransactionDTO> fraudDetectionService) {
        this.contextHandler = contextHandler;
        this.fraudDetectionService = fraudDetectionService;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The file data is partitioned based on hashed credit card. Different workers work on the
     * partitioned data in parallel to read the input, convert the input to transaction object and
     * then process the transactions for fraud detection. The output is finally written to the
     * console.
     */
    @Override
    public void processFile(final String file) {
        JavaSparkContext sparkContext = null;
        try {
            sparkContext = contextHandler.initializeContext(Constants.APP_NAME);
            JavaRDD<FraudDetailsDTO> fraudDetails = sparkContext.textFile(file)
                  .filter(input -> input.length() > 0)
                  .map(input -> getTransaction(input))
                  .mapToPair(transaction -> new Tuple2<>(transaction.getHashedCreditCard(), transaction))
                  .groupByKey()
                  .flatMap(transactionMap -> StreamSupport.stream(transactionMap._2.spliterator(), false)
                                                          .sorted((transactionA, transactionB) -> transactionA.getTimestamp().compareTo(transactionB.getTimestamp()))
                                                          .map(transaction -> fraudDetectionService.processTransaction(transaction))
                                                          .filter(Optional::isPresent)
                                                          .map(Optional::get)
                                                          .collect(Collectors.toSet())
                                                          .iterator());
            if (fraudDetails.isEmpty()) {
                System.out.println(Constants.NO_FRAUD_DETAILS_FOUND);
            } else {
                System.out.println(Constants.FRAUD_DETAILS_FOUND); 
                fraudDetails.foreach(
                    fraudCreditCard -> System.out.println(fraudCreditCard.getHashedCreditCard()));
            }
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.out.println(Constants.PROCESSING_ERROR);
        } finally {
            Optional.ofNullable(sparkContext).ifPresent(context -> contextHandler.closeContext(context));
        }
    }

    /**
     * Method to convert and get the transaction object from raw input record
     *
     * @param input the raw input record
     * @return the credit card transaction object
     * @throws AfterPayBusinessException in case of any validation failure for the raw input
     */
    private CreditCardTransactionDTO getTransaction(final String input) throws AfterPayBusinessException {
        final String[] transactionData = input.split(",");

        if (transactionData.length != 3) {
            System.out.println(MessageFormat.format(Constants.INVALID_TRANSACTION_RECORD, input));
            throw new AfterPayBusinessException(MessageFormat.format(Constants.INVALID_TRANSACTION_RECORD, input));
        }

        try {
            final LocalDateTime dateTime =
                DateTimeUtil.parse(transactionData[1].trim(), Constants.YYYY_MM_DD_T_HH_MM_SS);
            final BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(transactionData[2].trim()));
            return new CreditCardTransactionDTO(transactionData[0].trim(), dateTime, amount);
        } catch (final DateTimeParseException dtpException) {
            System.out.println(MessageFormat.format(Constants.INVALID_DATE, input));
            throw new AfterPayBusinessException(MessageFormat.format(Constants.INVALID_DATE, input), dtpException);
        } catch (final NumberFormatException nFException) {
            System.out.println(MessageFormat.format(Constants.INVALID_AMOUNT, input));
            throw new AfterPayBusinessException(MessageFormat.format(Constants.INVALID_AMOUNT, input), nFException);
        }
    }
}
