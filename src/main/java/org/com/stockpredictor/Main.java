package org.com.stockpredictor;

import com.google.common.collect.ImmutableList;
import org.com.stockpredictor.exception.InsufficientStockDataException;
import org.com.stockpredictor.model.StockData;
import org.com.stockpredictor.parsing.StockCsvParser;
import org.com.stockpredictor.parsing.StockCsvWriter;
import org.com.stockpredictor.prediction.StockPredictor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Entry point for the Stock Predictor application.
 */
public class Main {

    /**
     * Main method that orchestrates the reading of stock data from CSV files, prediction of future stock values,
     *  and writing the results.
     *
     * @param args command line arguments where args[0] is the path to the directory containing exchange folders,
     *   and args[1] is the maximum number of files to process per exchange.
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            Path exchangesFolder = Path.of(args[0]);
            int maxNumberOfFilesPerExchange;
            try {
                maxNumberOfFilesPerExchange = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.printf("Unable to parse the maximum number of files per exchange program argument: %s", e.getMessage());
                return;
            }
            StockCsvParser stockCsvParser = new StockCsvParser(new Random());
            List<File> eligibleFiles = findEligibleFiles(exchangesFolder, maxNumberOfFilesPerExchange);
            for (File file : eligibleFiles) {
                try {
                    List<StockData> randomConsecutiveStockData = stockCsvParser.extractRandomConsecutiveStockData(file);

                    List<StockData> predictions = StockPredictor.predict(randomConsecutiveStockData);
                    List<StockData> results = Stream.concat(randomConsecutiveStockData.stream(), predictions.stream())
                            .collect(toImmutableList());
                    StockCsvWriter.write(results, file.getName());
                } catch (InsufficientStockDataException e) {
                    System.err.println(e.getMessage());
                }
            }
        } else {
            System.err.println("Invalid number of arguments");
        }
    }

    /**
     * Finds and collects a limited number of eligible CSV files from each exchange directory within the specified folder.
     *
     * @param folder the path to the parent directory containing exchange-specific directories
     * @param maxFilesPerExchange the maximum number of CSV files to include from each exchange directory
     * @return a list of eligible CSV files to be processed
     */
    private static List<File> findEligibleFiles(Path folder, int maxFilesPerExchange) {
        ImmutableList.Builder<File> eligibleFiles = ImmutableList.builder();
        try {
            Set<Path> exchangeDirectories = Files.walk(folder, 1)
                    .filter(Files::isDirectory)
                    .filter(path -> !path.equals(folder))
                    .collect(toImmutableSet());
            for (Path exchangeDirectory : exchangeDirectories) {
                List<File> exchangeFiles = Files.walk(exchangeDirectory, 1)
                        .filter(path -> !path.equals(exchangeDirectory))
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".csv"))
                        .limit(maxFilesPerExchange)
                        .map(Path::toFile)
                        .collect(toImmutableList());

                eligibleFiles.addAll(exchangeFiles);
            }
        } catch (IOException e) {
            System.err.printf("Unexpected I/O exception: %s", e.getMessage());
        }
        return eligibleFiles.build();
    }
}