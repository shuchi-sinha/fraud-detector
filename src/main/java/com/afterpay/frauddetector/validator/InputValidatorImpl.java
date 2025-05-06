package com.afterpay.frauddetector.validator;

import java.io.File;

import com.afterpay.frauddetector.constants.Constants;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

@Component
public class InputValidatorImpl implements InputValidator {

    @Override
    public void validateFile(final String inputFile) {
        final File file = new File(inputFile);

        if (!(file.exists() && file.isFile() && FilenameUtils.isExtension(inputFile, Constants.VALID_EXTENSIONS))) {
            throw new IllegalArgumentException(Constants.INVALID_FILENAME);
        }
    }

    @Override
    public void validateThreshold(final String threshold) {

        try {
            Double.parseDouble(threshold);
        } catch (final NumberFormatException nfException) {
            throw new IllegalArgumentException(Constants.INVALID_THRESHOLD_AMOUNT, nfException);
        }
    }
}
