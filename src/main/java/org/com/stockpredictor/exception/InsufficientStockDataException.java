package org.com.stockpredictor.exception;

/**
 * Exception thrown when insufficient stock data is encountered during parsing of CSV files.
 */
public class InsufficientStockDataException extends Exception {
    public InsufficientStockDataException(String message) {
        super(message);
    }
}
