# Hackathon - Credit Card Fraud Detector
The Credit Card Fraud application analyses the transaction data over a time window and detects the fraud cards.

**Technology Stack used:**
* Java 11
* Spring Boot
* Apache Spark

# Getting Started

### Application Prerequisites
- Java 11
- Maven

### Assumptions Taken
1. The combination of hashed credit card and timestamp will always be unique in the csv file, i.e. only single transaction can happen for a credit card at a time. 
   **Note**: For future, a unique transaction ID can be added in the csv file and can be used to overcome this assumption.
2. In case there is a fraud for same card multiple times, it will be printed in the output multiple times.
3. A single transaction price will always be less than or equal to threshold price.

### File Format

The csv file should comprise of a comma separated string of below elements, one per line. All the fields are mandatory.
A credit card transaction comprises the following elements.
* hashed credit card number
* timestamp - of format year-month-dayThour:minute:second
* amount - of format dollars.cents

Example:
```
10d7ce2f43e35fa57d1bbf8b1e2,2014-04-29T13:15:54,10.00
```

A credit card will be identified as fraudulent if the sum of amounts for a unique hashed credit
card number over a 24-hour sliding window period exceeds the threshold amount passed in the arguments.

The application will print the fraudulent cards if any and will then exit.

### File Location

The default location of the file has been set to be the 'input/' folder. It can be changed by either of the below two options.
* Change property file-location in the application.properties and putting the relative path to the csv file.
* Pass an additional argument 'file-location' at the time of running application.

Note: The file-location property or argument needs a trailing slash.

```
--file-location=<path>/
```

### Running the Application

In order to run the application, you can use the below command.
```
mvn -q spring-boot:run -Dspring-boot.run.arguments="--threshold=<threshold-value> --filename=<filename-value>"

```
You can also run the application by creating the Snapshot JAR file and using the same.

```
mvn clean install
java -jar <path>/fraud-detector-1.0.0-SNAPSHOT.jar  --threshold=<threshold-value> --filename=<filename-value>
```

The below arguments are mandatory and need to be passed in the above commands.
1. threshold - The threshold amount to be used for fraud detection
2. filename - The name of the csv file which holds the credit card transactions to be processed.


##### Fraud Window Settings
The fraud window time has been made configurable and can be updated in the application.properties if required in future.
```
fraud.detection.window=<value in defined chrono unit>
fraud.detection.chrono-unit=<Chrono unit enum value>
```
Please refer to [Chrono Unit](https://docs.oracle.com/javase/8/docs/api/java/time/temporal/ChronoUnit.html) for possible enum values.

## Application Design
The application is based on resilient distributed design to detect credit card frauds happening within a specified timeframe. It mainly consists of two components:
* Transaction File Processing: The transaction file is read with Spark in different partitions parallelly. Different spark workers read the transactions and group them by the hashed credit card number in parallel. The transactions are then processed in parallel by the workers for fraud detection and output is provided.
* Sliding Window: The sliding window stores the previous transaction (applicable for the sliding fraud window) in a sorted set for each hashed credit card. For each transaction, the outdated transactions which are out of the sliding fraud window, are removed from the set for efficient memory usage. The cache also keeps a record of the total transaction price for the sliding fraud window for each hashed card.
Every transaction is processed and then total is compared with the fraud threshold to detect the fraud.
Note: Java in-memory maps have been used as cache in the applicaton as of now. It can be switched to Redis, Memcache or any other optimized cache in future easily.


## Application Logs
All the application logs will be printed to the console.log file in the 'logs' folder. The path of the log file can be updated by changing the below property.  The log file will be moved to <logging.folder.path>/archive folder on a daily basis as per the logging configuration.
 ```
 logging.folder.path=logs
 ```

## Important Points

1. The output has been printed to console using System.out.println. Hence when you run the unit test cases, the output for success and error scenarios gets printed in the console output itself. These messages need to be ignored as of now.
2. While running the application, you will get Warning logs for "Illegal Reflective Access" of JDK library by Spark libraries. It will not impact the running of the application and can be ignored. The same has already been raised to Spark and will be fixed in future Spark releases.
```
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.apache.spark.unsafe.Platform (file:/C:/Users/shuchi/.m2/repository/org/apache/spark/spark-unsafe_2.12/3.0.1/spark-unsafe_2.12-3.0.1.jar) to constructor java.nio.DirectByteBuffer(long,int)
WARNING: Please consider reporting this to the maintainers of org.apache.spark.unsafe.Platform
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
```

3. In case you are running the application on Windows machine, you may see Error logs related to winutils binary for hadoop. It will not impact the running of the application and can be ignored. The same has already been raised to Spark as well as Hadoop and will be fixed in future Spark releases. As a workaround, the winutils.exe can be manually downloaded and hadoop home path can be pointed to it.
```
05-01-2021 21:13:18.976 [main] ERROR org.apache.hadoop.util.Shell.getWinUtilsPath - Failed to locate the winutils binary in the hadoop binary path
java.io.IOException: Could not locate executable null\bin\winutils.exe in the Hadoop binaries
```
