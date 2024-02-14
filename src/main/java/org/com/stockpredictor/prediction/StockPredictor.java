package org.com.stockpredictor.prediction;

import org.com.stockpredictor.model.StockData;
import org.com.stockpredictor.model.StockDataUtils;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 *
 */
public class StockPredictor {

    /**
     * Predicts the next 3 data points given the historical data.
     *
     * @param historicalData the historical data
     * @return a list of the 3 new predicted stock data points
     */
    public static List<StockData> predict(List<StockData> historicalData) {
        StockData lastStockData = historicalData.get(historicalData.size() - 1);
        double firstPrice = predictFirstPrice(historicalData);
        double secondPrice = predictSecondPrice(lastStockData.getPrice(), firstPrice);
        double thirdPrice = predictThirdPrice(firstPrice, secondPrice);
        return StockDataUtils.createStockDataList(
                lastStockData.getId(),
                lastStockData.getTimestamp(),
                firstPrice, secondPrice, thirdPrice);
    }

    /**
     * Predicts the first new price given the historical data by finding the 2nd highest price in the historical data.
     *
     * @param historicalData the stock data we want to predict the next data point for
     * @return the price of the next data point
     */
    static double predictFirstPrice(List<StockData> historicalData) {
        Set<Double> prices = historicalData.stream()
                .map(StockData::getPrice)
                .collect(toImmutableSet());
        double highest = Double.MIN_VALUE;
        double secondHighest = Double.MIN_VALUE;

        //if all prices are the same then we predict the same price.
        if (prices.size() == 1) {
            return prices.stream().findFirst().get();
        }

        for (double price : prices) {
            if (price > highest) {
                secondHighest = highest;
                highest = price;
            } else if (price > secondHighest) {
                secondHighest = price;
            }
        }

        return secondHighest;
    }

    /**
     * Predicts the 2nd next data point price based on the following formula: the last price + the difference between
     *  our 1st predicted price and the last price in the historical data. This simplifies in the average between the two.
     *
     * @param lastPrice the last price in the historical data
     * @param firstPredictedPrice the first new predicted data point price
     * @return the price of the second predicted data point
     */
    static double predictSecondPrice(double lastPrice, double firstPredictedPrice) {
        return (lastPrice + firstPredictedPrice) / 2;
    }

    /**
     * Predicts the 3rd next data point price based on the following formula: the 1st predicted price + a fourth of
     * the difference between the 2nd and 1st predicted prices.
     *
     * @param firstPredictedPrice the first new predicted data point price
     * @param secondPredictedPrice the second predicted data point price
     * @return the price of the third predicted data point
     */
    static double predictThirdPrice(double firstPredictedPrice, double secondPredictedPrice) {
        return firstPredictedPrice + (secondPredictedPrice - firstPredictedPrice) / 4;
    }
}
