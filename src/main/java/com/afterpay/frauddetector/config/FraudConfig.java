package com.afterpay.frauddetector.config;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class FraudConfig implements Serializable {

    private static final long serialVersionUID = -169067350144248901L;
    private final Long window;
    private final ChronoUnit chronoUnit;
    private final BigDecimal amount;

    public FraudConfig(final long window, final ChronoUnit chronoUnit, final BigDecimal amount) {
        this.window = window;
        this.chronoUnit = chronoUnit;
        this.amount = amount;
    }

    public Long getWindow() {
        return window;
    }

    public ChronoUnit getChronoUnit() {
        return chronoUnit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
