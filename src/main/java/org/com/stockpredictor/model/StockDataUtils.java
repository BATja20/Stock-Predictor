package org.com.stockpredictor.model;

import com.google.common.collect.ImmutableList;

import java.time.LocalDate;
import java.util.List;

/**
 * Utility class for operations related to {@link StockData} objects.
 */
public class StockDataUtils {

    /**
     * Creates a list of {@link StockData} for a given stock over consecutive days.
     *
     * @param stockId the ID of the stock
     * @param startDate the starting date for the stock data series, exclusive
     * @param prices a list of prices we want to generate the consecutive stock data for
     * @return a list of stock data over consecutive days
     */
    public static List<StockData> createStockDataList(String stockId, LocalDate startDate, double... prices) {
        ImmutableList.Builder<StockData> stockDataListBuilder = ImmutableList.builder();
        for (int i = 0; i < prices.length; i++) {
            StockData stockData = StockData.of(stockId, startDate.plusDays(i + 1), prices[i]);
            stockDataListBuilder.add(stockData);
        }
        return stockDataListBuilder.build();
    }
}
