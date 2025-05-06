package com.afterpay.frauddetector.handler;

import org.apache.spark.api.java.JavaSparkContext;

/**
 * Interface to initialize and close the Spark Context for input data processing
 *
 * @param <T> type of datasets for which spark context needs to be created
 * @author shuchi
 */
public interface SparkContextHandler {

    /**
     * Method to initialize a new Spark context for data processing
     *
     * @return the spark context
     */
    public JavaSparkContext initializeContext(final String appName);

    /**
     * Method to close the spark context
     */
    public void closeContext(JavaSparkContext sparkContext);
}
