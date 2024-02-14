package org.com.stockpredictor.model;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single stock data point.
 */
@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
@Value
public class StockData {

    /**
     * Formatter for stock data timestamps in "dd-MM-yyyy" format.
     */
    public static final DateTimeFormatter TIMESTAMP_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Unique identifier for the stock.
     */
    String id;

    /**
     * Date of the stock data point.
     */
    LocalDate timestamp;

    /**
     * Price of the stock at the given timestamp.
     */
    double price;

    public static StockData of(String stockId, LocalDate timestamp, double price) {
        return new StockData(stockId, timestamp, price);
    }

    /**
     * Converts this stock data point to a CSV row format.
     *
     * @return a CSV-formatted string representing this stock data point
     */
    public String toCsvRow() {
        return id + "," + timestamp.format(TIMESTAMP_DATE_FORMATTER) + "," + price + "\n";
    }
}
