package org.com.stockpredictor.parsing;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import org.com.stockpredictor.exception.InsufficientStockDataException;
import org.com.stockpredictor.model.StockData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockCsvParserTest {
    private static final String STOCK_ID = "TSLA";
    private final ByteArrayOutputStream errorContent = new ByteArrayOutputStream();
    private final PrintStream originalError = System.err;

    @Before
    public void setUpStreams() {
        System.setErr(new PrintStream(errorContent));
    }

    @After
    public void restoreStreams() {
        System.setErr(originalError);
    }

    @Test
    public void testExtractCorrectly() throws URISyntaxException, InsufficientStockDataException {
        File file = new File(Resources.getResource("TSLA.csv").toURI());
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(3);
        List<StockData> stockDataList = new StockCsvParser(mockRandom).extractRandomConsecutiveStockData(file);
        assertThat(stockDataList)
                .hasSize(10)
                .isEqualTo(ImmutableList.builder()
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 4), 193.20))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 5), 191.84))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 6), 194.14))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 7), 192.20))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 8), 192.20))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 9), 193.93))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 10), 195.87))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 11), 194.50))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 12), 196.64))
                        .add(StockData.of(STOCK_ID, LocalDate.of(2023, 9, 13), 194.87))
                        .build());
    }

    @Test
    public void testExtractAllDataCorrectly() throws URISyntaxException {
        File file = new File(Resources.getResource("TSLA.csv").toURI());
        StockCsvParser stockCsvParser = new StockCsvParser(new Random());
        List<StockData> stockDataList = stockCsvParser.extractAllStockData(file);
        assertThat(stockDataList).hasSize(106);
    }

    @Test
    public void testExtractAndLogIncorrectData() throws URISyntaxException {
        File file = new File(Resources.getResource("TSLA-bad.csv").toURI());
        StockCsvParser stockCsvParser = new StockCsvParser(new Random());
        List<StockData> stockDataList = stockCsvParser.extractAllStockData(file);
        assertThat(stockDataList).hasSize(0);
        assertThat(errorContent.toString())
                .contains("Unable to parse price at row: 1")
                .contains("Unable to parse date time at row: 2")
                .contains("Unable to parse stock ID at row: 3, due to being empty");
    }
}