package com.afterpay.frauddetector.handler.impl;

import com.afterpay.frauddetector.handler.SparkContextHandler;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

class SparkContextHandlerImplTest {

    public static final String APP_NAME = "appName";

    SparkContextHandler sparkContextHandler;
    private static JavaSparkContext sparkContext;

    @BeforeEach
    public void init() {
        sparkContextHandler = new SparkContextHandlerImpl();
    }

    @Test
    void testInitializeContext() {
        //when
        sparkContext = sparkContextHandler.initializeContext(APP_NAME);

        //then
        assertEquals(sparkContext.appName(), APP_NAME);
        sparkContext.close();
    }

    @Test
    void testCloseContext() {
        final SparkConf conf = new SparkConf();
        conf.setMaster("local[2]");
        conf.setAppName(APP_NAME);
        JavaSparkContext context = new JavaSparkContext(conf);
        sparkContextHandler.closeContext(context);

        assertThrows(IllegalStateException.class, () -> context.textFile("abc"));
    }

    @AfterAll
    public static void close() {
        sparkContext.close();
    }
}