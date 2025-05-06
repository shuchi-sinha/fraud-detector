package com.afterpay.frauddetector.handler.impl;

import java.io.Serializable;

import com.afterpay.frauddetector.constants.Constants;
import com.afterpay.frauddetector.handler.SparkContextHandler;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link SparkContextHandler} to read data from local file system
 *
 * @author shuchi
 */
@Component
public class SparkContextHandlerImpl implements SparkContextHandler, Serializable {

    private static final long serialVersionUID = 4704575839318844057L;


    @Override
    public JavaSparkContext initializeContext(final String appName) {
        return getJavaSparkContext(appName, true);
    }

    @Override
    public void closeContext(final JavaSparkContext sparkContext) {
        sparkContext.close();
    }

    private JavaSparkContext getJavaSparkContext(final String appName, final boolean islocal) {
        final SparkConf sparkConf = new SparkConf().setAppName(appName);
        if (islocal) {
            sparkConf.setMaster(Constants.LOCAL_SPARK_MASTER);
        }
        return new JavaSparkContext(sparkConf);
    }
}
