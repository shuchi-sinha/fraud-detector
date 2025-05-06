package com.afterpay.frauddetector.file.processor.impl;

import java.util.ArrayList;
import java.util.List;

import com.afterpay.frauddetector.dto.creditcard.CreditCardTransactionDTO;
import com.afterpay.frauddetector.handler.SparkContextHandler;
import com.afterpay.frauddetector.service.FraudDetectionService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SparkInputFileProcessorImplTest {

    public static final String APP_NAME = "FraudDetector";
    private static JavaRDD<String> rdd;
    private static JavaSparkContext context;

    @Mock
    SparkContextHandler contextHandler;

    @Mock(serializable = true)
    FraudDetectionService<CreditCardTransactionDTO> fraudDetectionService;

    @Mock
    private JavaSparkContext mockedContext;

    @InjectMocks
    SparkInputFileProcessorImpl inputFileProcessor;

    @BeforeAll
    public static void init() throws IllegalArgumentException {
        final SparkConf conf = new SparkConf();
        conf.setMaster("local[2]");
        conf.setAppName(APP_NAME);
        context = new JavaSparkContext(conf);
    }

    @Test
    void processFile() {
        //given
        final List<String> file = new ArrayList<>();
        file.add("10d7ce2f43e35fa57d1bbf8b1e2,2014-04-29T13:15:54,10.00");
        rdd = context.parallelize(file,1);

        when(contextHandler.initializeContext(APP_NAME)).thenReturn(mockedContext);
        when(mockedContext.textFile("fileName")).thenReturn(rdd);

        //when
        inputFileProcessor.processFile("fileName");

        //then
        verify(contextHandler).closeContext(mockedContext);
    }

    @Test
    void testProcessFileWithInvalidTransactionRecord() {
        //given
        final List<String> file = new ArrayList<>();
        file.add("10d7ce2f43e35fa57d1bbf8b1e2,2014-04-29T13:15:54");
        rdd = context.parallelize(file,1);
        when(contextHandler.initializeContext(APP_NAME)).thenReturn(mockedContext);
        when(mockedContext.textFile("fileName")).thenReturn(rdd);

        //when
        inputFileProcessor.processFile("fileName");

        //then
        /*in case of error we just log it on console*/
        verify(contextHandler).closeContext(mockedContext);

    }

    @Test
    void testProcessFileWithInvalidDate() {
        //given
        final List<String> file = new ArrayList<>();
        file.add("10d7ce2f43e35fa57d1bbf8b1e2,2014-04-29H13:15:54,10.00");
        rdd = context.parallelize(file,1);
        when(contextHandler.initializeContext(APP_NAME)).thenReturn(mockedContext);
        when(mockedContext.textFile("fileName")).thenReturn(rdd);

        //when
        inputFileProcessor.processFile("fileName");

        //then
        /*in case of error we just log it on console*/
        verify(contextHandler).closeContext(mockedContext);

    }

    @Test
    void testProcessFileWithInvalidAmount() {
        //given
        final List<String> file = new ArrayList<>();
        file.add("10d7ce2f43e35fa57d1bbf8b1e2,2014-04-29T13:15:54,abc");
        rdd = context.parallelize(file,1);
        when(contextHandler.initializeContext(APP_NAME)).thenReturn(mockedContext);
        when(mockedContext.textFile("fileName")).thenReturn(rdd);

        //when
        inputFileProcessor.processFile("fileName");

        //then
        /*in case of error we just log it on console*/
        verify(contextHandler).closeContext(mockedContext);

    }

    @AfterAll
    public static void close() {
        context.close();
    }
}
