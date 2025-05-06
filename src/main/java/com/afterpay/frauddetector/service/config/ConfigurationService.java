package com.afterpay.frauddetector.service.config;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import com.afterpay.frauddetector.config.FraudConfig;
import com.afterpay.frauddetector.validator.InputValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fraud.detection")
public class ConfigurationService {

    private final InputValidator inputValidator;
    private long window;
    private String chronoUnit;

    ConfigurationService(final InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    @Bean
    public FraudConfig getFraudConfig(@Value("${threshold}") final String threshold) {
        try {
            inputValidator.validateThreshold(threshold);
            return new FraudConfig(window, ChronoUnit.valueOf(chronoUnit), BigDecimal.valueOf(Double.parseDouble(threshold)));
        } catch (final IllegalArgumentException iAException) {
            System.out.println(iAException.getMessage());
            throw iAException;
        }
    }

    public void setWindow(final long window) {
        this.window = window;
    }

    public void setChronoUnit(final String chronoUnit) {
        this.chronoUnit = chronoUnit;
    }
}
