package org.com.stockpredictor.parsing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.com.stockpredictor.exception.InsufficientStockDataException;
import org.com.stockpredictor.model.StockData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableList.toImmutableList;

@AllArgsConstructor
@Getter
public class StockCsvParser {

    private final Random random;
    private static final int REQUIRED_SIZE = 10;

    /**
     * Extracts a number of consecutive stock data rows from a given CSV files starting from a random one.
     *
     * @param file the given file
     * @return the random consecutive stock data rows
     * @throws InsufficientStockDataException when we cannot find sufficient data in order to make a prediction.
     */
    public List<StockData> extractRandomConsecutiveStockData(File file) throws InsufficientStockDataException {
        List<StockData> allStockData = extractAllStockData(file);
        if (allStockData.size() >= REQUIRED_SIZE) {
            int randomIndex = random.nextInt(allStockData.size() - REQUIRED_SIZE);
            return allStockData.subList(randomIndex, randomIndex + REQUIRED_SIZE);
        } else {
            throw new InsufficientStockDataException("Could not find enough stock data");
        }
    }

    /**
     * Parses the CSV file line by line, ignoring rows that cannot be parsed.
     *
     * @param file the specified file
     * @return the parsed list of stock data.
     */
    List<StockData> extractAllStockData(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            List<String> lines = bufferedReader.lines().collect(toImmutableList());
            return Streams.zip(IntStream.range(1, lines.size() + 1).boxed(), lines.stream(), this::convertLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toImmutableList());
        } catch (FileNotFoundException e) {
            System.err.printf("Could not find file: %s, with error: %s\n", file.getName(), e);
            return ImmutableList.of();
        }
    }

    private Optional<StockData> convertLine(int index, String line) {
        String[] values = line.split(",");
        if (values.length < 3) {
            System.err.printf("Unable to parse price row with index %s, due to insufficient data\n", index);
            return Optional.empty();
        }

        String stockId = values[0];
        if (stockId.isEmpty()) {
            System.err.printf("Unable to parse stock ID at row: %s, due to being empty\n", index);
            return Optional.empty();
        }

        LocalDate timestamp;
        double price;
        try {
             price = Double.parseDouble(values[2]);
             timestamp = LocalDate.parse(values[1], StockData.TIMESTAMP_DATE_FORMATTER);
        } catch (NumberFormatException e) {
            System.err.printf("Unable to parse price at row: %s\n", index);
            return Optional.empty();
        } catch (DateTimeParseException e) {
            System.err.printf("Unable to parse date time at row: %s\n", index);
            return Optional.empty();
        }

        return Optional.of(StockData.of(stockId, timestamp, price));
    }
}
