package com.afterpay.frauddetector.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.afterpay.frauddetector.constants.Constants;
import com.afterpay.frauddetector.file.processor.InputFileProcessor;
import com.afterpay.frauddetector.validator.InputValidator;

@Component
public class FraudDetectorApplicationRunner implements ApplicationRunner {

    private final InputFileProcessor fileProcessor;

    private final InputValidator inputValidator;

    @Value("${filename}")
    private String filename;

    @Value("${file-location}")
    private String fileLocation;

    @Value("${threshold}")
    private String threshold;

    @Autowired
    FraudDetectorApplicationRunner(final InputFileProcessor fileProcessor, final InputValidator inputValidator) {
        this.fileProcessor = fileProcessor;
        this.inputValidator = inputValidator;
    }

    @Override
    public void run(final ApplicationArguments args) {
        if (!(args.containsOption("filename") && args.containsOption("threshold"))) {
            System.out.println(Constants.INVALID_ARGUMENTS);
            throw new IllegalArgumentException(Constants.INVALID_ARGUMENTS);
        }

        try {
            inputValidator.validateFile(fileLocation + filename);
            inputValidator.validateThreshold(threshold);
            fileProcessor.processFile(fileLocation + filename);
        } catch (final IllegalArgumentException iAException) {
            System.out.println(iAException.getMessage());
            throw iAException;
        }
    }
}
