package com.afterpay.frauddetector.validator;

public interface InputValidator {

    void validateThreshold(String threshold);

    void validateFile(String file);
}
