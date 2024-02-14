package org.com.stockpredictor.prediction;

import org.com.stockpredictor.model.StockData;
import org.com.stockpredictor.model.StockDataUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class StockPredictorTest {
    static Stream<Arguments> pricesWithExpectedValueForFirst() {
        return Stream.of(
                arguments(new double[]{1d, 2d, 3d, 4d, 5d, 6d, 7d}, 6d),
                arguments(new double[]{1d, 1d, 1d, 1d, 1d, 1d, 1d}, 1d),
                arguments(new double[]{1d}, 1d),
                arguments(new double[]{1d, 2d}, 1d),
                arguments(new double[]{3d, 2d}, 2d),
                arguments(new double[]{3d, 2d, 6d, 100d, -1d, 231d, 42d, 199d}, 199d),
                arguments(new double[]{3d, 2d, 1d}, 2d));
    }

    static Stream<Arguments> pricesWithExpectedForSecond() {
        return Stream.of(
                arguments(1d, 6d, 3.5d),
                arguments(6d, 1d, 3.5d));
    }

    static Stream<Arguments> pricesWithExpectedForThird() {
        return Stream.of(
                arguments(1d, 5d, 2d),
                arguments(6d, 2d, 5d));
    }

    @ParameterizedTest
    @MethodSource("pricesWithExpectedValueForFirst")
    public void testPredictFirstPriceCorrect(double[] prices, double expected) {
        List<StockData> historicalData = StockDataUtils.createStockDataList(
                "Mock",
                LocalDate.of(2024, 1, 1), prices);

        double firstPrice = StockPredictor.predictFirstPrice(historicalData);
        assertThat(firstPrice).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("pricesWithExpectedForSecond")
    public void testPredictSecondPriceCorrect(double lastPrice, double firstPrice, double expected) {
        double secondPrice = StockPredictor.predictSecondPrice(lastPrice, firstPrice);
        assertThat(secondPrice).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("pricesWithExpectedForThird")
    public void testPredictThirdPriceCorrect(double firstPrice, double secondPrice, double expected) {
        double thirdPrice = StockPredictor.predictThirdPrice(firstPrice, secondPrice);
        assertThat(thirdPrice).isEqualTo(expected);
    }
}