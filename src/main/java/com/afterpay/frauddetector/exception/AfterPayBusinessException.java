package com.afterpay.frauddetector.exception;

public class AfterPayBusinessException extends Exception {

    private static final long serialVersionUID = 6883795102247787430L;

    public AfterPayBusinessException(final String message) {
        super(message);
    }

    public AfterPayBusinessException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
