package com.afterpay.frauddetector.constants;

/**
 * This class keeps the static list of all the constants being used in the application.
 *
 * @author shuchi
 */
public class Constants {
  public static final String LOCAL_SPARK_MASTER = "local[*]";
  
  public static final String APP_NAME = "FraudDetector";
  
  public static final long ZERO = 0;

  public static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";

  public static final String INVALID_AMOUNT = "Error in processing due to invalid amount found for the transaction record.\n{0}";

  public static final String INVALID_DATE = "Error in processing due to invalid date found for the transaction record.\n{0}";

  public static final String INVALID_TRANSACTION_RECORD = "Error in processing due to invalid transaction record found in the file.\n{0}";

  public static final String CACHE_DATA_IS_NOT_IN_SYNC =
      "The cache data is not in sync for credit card {0}. Please check the debug logs for further details.";
  
  public static final String INVALID_THRESHOLD_AMOUNT =
      "The threshold amount provided is invalid. Please try again wih a valid value.";
  
  public static final String VALID_EXTENSIONS = "csv";
  
  public static final String INVALID_FILENAME =
      "Either filename is invalid or file does not exist at specified path. Please verify and pass a valid filename for a csv file.";

  public static final String INVALID_ARGUMENTS =
      "Please pass a valid filename and threshold value as below. \n"
          + "mvn -q spring-boot:run -Dspring-boot.run.arguments=\"--threshold=<threshold-value> --filename=<filename-value>\" \n\n"
          + "In case you are running it via JAR file, use: \njava -jar <path>/fraud-detector-1.0.0-SNAPSHOT.jar  --threshold=<threshold-value> --filename=<filename-value>";
  
  public static final String NO_FRAUD_DETAILS_FOUND = "****No Fraud cards found!!****";
  
  public static final String FRAUD_DETAILS_FOUND = "****Please find below the Fraud Card Details.****";

  public static final String PROCESSING_ERROR = "An error has occurred while processing the file. Please check the log file for further details.";

  private Constants() {
    // Added to avoid object instantiation
  }
}
