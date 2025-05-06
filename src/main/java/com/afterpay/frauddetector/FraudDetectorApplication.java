package com.afterpay.frauddetector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.afterpay")
public class FraudDetectorApplication {

    public static void main(final String[] args) {
        SpringApplication.run(FraudDetectorApplication.class, args);
    }
}
