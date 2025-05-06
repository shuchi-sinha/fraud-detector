package com.afterpay.frauddetector.runner;

import com.afterpay.frauddetector.file.processor.InputFileProcessor;
import com.afterpay.frauddetector.validator.InputValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FraudDetectorApplicationRunnerTest {

    @Mock
    InputValidator inputValidator;

    @Mock
    InputFileProcessor inputFileProcessor;

    @Mock
    ApplicationArguments applicationArguments;

    @InjectMocks
    FraudDetectorApplicationRunner fraudDetectorApplicationRunner;

    @Test
    void testRunWithEmptyFileName() {
        //given
        when(applicationArguments.containsOption("filename")).thenReturn(false);

        //when //then
        assertThrows(IllegalArgumentException.class, () -> fraudDetectorApplicationRunner.run(applicationArguments));
    }

    @Test
    void testRunWithEmptyThreshold() {
        //given
        when(applicationArguments.containsOption("filename")).thenReturn(true);
        when(applicationArguments.containsOption("threshold")).thenReturn(false);

        //when //then
        assertThrows(IllegalArgumentException.class, () -> fraudDetectorApplicationRunner.run(applicationArguments));
    }

    @Test
    void testRunWithInvalidFileName() {
        //given
        when(applicationArguments.containsOption("filename")).thenReturn(true);
        when(applicationArguments.containsOption("threshold")).thenReturn(true);
        doThrow(IllegalArgumentException.class).when(inputValidator).validateFile(any());

        //when //then
        assertThrows(IllegalArgumentException.class, () -> fraudDetectorApplicationRunner.run(applicationArguments));
    }

    @Test
    void testRunWithInvalidThreshold() {
        //given
        when(applicationArguments.containsOption("filename")).thenReturn(true);
        when(applicationArguments.containsOption("threshold")).thenReturn(true);
        doNothing().when(inputValidator).validateFile(any());
        doThrow(IllegalArgumentException.class).when(inputValidator).validateThreshold(any());

        //when //then
        assertThrows(IllegalArgumentException.class, () -> fraudDetectorApplicationRunner.run(applicationArguments));
    }

    @Test
    void testRunWithValidArguments() {
        //given
        when(applicationArguments.containsOption("filename")).thenReturn(true);
        when(applicationArguments.containsOption("threshold")).thenReturn(true);
        doNothing().when(inputValidator).validateFile(any());
        doNothing().when(inputValidator).validateThreshold(any());
        doNothing().when(inputFileProcessor).processFile(any());

        //when
        fraudDetectorApplicationRunner.run(applicationArguments);

        // then
        verify(inputFileProcessor).processFile(any());
    }
}