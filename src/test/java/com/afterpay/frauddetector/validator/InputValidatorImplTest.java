package com.afterpay.frauddetector.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputValidatorImplTest {

    InputValidator inputValidator;

    @BeforeEach
    public void init() {
        inputValidator = new InputValidatorImpl();
    }

    @Test
    void testValidateFileForNonExistingFile() {
        assertThrows(IllegalArgumentException.class, () -> inputValidator.validateFile(""));
    }

    @Test
    void testValidateFileForDirectory() {
        assertThrows(IllegalArgumentException.class, () -> inputValidator.validateFile("src"));
    }

    @Test
    void testValidateFileForInvalidFileExtension() {
        assertThrows(IllegalArgumentException.class, () -> inputValidator.validateFile("src/test/resources/invalid.txt"));
    }

    @Test
    void testValidateFileForValidFile() {
        assertDoesNotThrow(() -> inputValidator.validateFile("src/test/resources/test.csv"));
    }

    @Test
    void validateThresholdForInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> inputValidator.validateThreshold("abc"));
    }

    @Test
    void validateThresholdForValidInput() {
        assertDoesNotThrow(() -> inputValidator.validateThreshold("22.00"));
    }

}