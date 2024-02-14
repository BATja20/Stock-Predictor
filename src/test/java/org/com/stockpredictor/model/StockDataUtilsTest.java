package org.com.stockpredictor.model;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StockDataUtilsTest {

    private static final String STOCK_ID = "TSLA";

    @Test
    public void testCreatesStockDataList() {

        List<StockData> stockDataList = StockDataUtils.createStockDataList(
                STOCK_ID,
                LocalDate.of(2023, 9, 3),
                193.20, 191.84, 194.14, 192.20, 192.20, 193.93, 195.87, 194.50, 196.64, 194.87);

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

}