package org.com.stockpredictor.parsing;

import org.com.stockpredictor.model.StockData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Handles writing of stock data predictions to CSV files.
 */
public class StockCsvWriter {

    /**
     * Writes the predicted stock data in a file located in a current folder.
     *
     * @param results the resulting stock data
     * @param fileName the file name
     */
    public static void write(List<StockData> results, String fileName) {
        try {
            String csvContent = results.stream()
                .map(StockData::toCsvRow)
                .reduce("", String::concat);
            Path filePath = Path.of("./" + fileName);
            Files.writeString(filePath, csvContent);
        } catch (IOException e) {
            System.err.printf("Unable to write results for file: %s, with error: %s", fileName, e);
        }
    }
}
