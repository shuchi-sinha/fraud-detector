package com.afterpay.frauddetector.dto.creditcard;

import java.io.Serializable;

/**
 * The fraud details data transfer object
 *
 * @author shuchi
 */
public class FraudDetailsDTO implements Serializable {

  private static final long serialVersionUID = -2415667843716802591L;
  
  private String hashedCreditCard;

    public FraudDetailsDTO(final String hashedCreditCard) {
        this.hashedCreditCard = hashedCreditCard;
    }

    public String getHashedCreditCard() {
        return hashedCreditCard;
    }

    public void setHashedCreditCard(final String hashedCreditCard) {
        this.hashedCreditCard = hashedCreditCard;
    }
}
