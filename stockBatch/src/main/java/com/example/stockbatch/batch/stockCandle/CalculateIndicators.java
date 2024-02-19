package com.example.stockbatch.batch.stockCandle;

import com.example.stockbatch.domain.StockCandle;

import java.util.List;

public class CalculateIndicators {
    public static void calculateIndicators(List<StockCandle> stockCandles) {
        calculateMovingAverages(stockCandles);
        calculateBollingerBands(stockCandles);
        calculateMACD(stockCandles);
    }

    private static void calculateMovingAverages(List<StockCandle> stockCandles) {
        calculateMovingAverage(stockCandles, 12);
        calculateMovingAverage(stockCandles, 20);
        calculateMovingAverage(stockCandles, 26);
    }

    private static void calculateMovingAverage(List<StockCandle> stockCandles, int days) {
        for (int i = days - 1; i < stockCandles.size(); i++) {
            double sum = 0;
            for (int j = i; j > i - days; j--) {
                sum += stockCandles.get(j).getClose(); // 가격은 종가를 사용한다고 가정
            }
            double average = sum / days;
            switch (days) {
                case 12:
                    stockCandles.get(i).setMovingAverage_12(average);
                    break;
                case 20:
                    stockCandles.get(i).setMovingAverage_20(average);
                    break;
                case 26:
                    stockCandles.get(i).setMovingAverage_26(average);
                    break;
            }
        }
    }

    private static void calculateBollingerBands(List<StockCandle> stockCandles) {
        int period = 20; // 볼린저 밴드 주기
        double multiplier = 2; // 표준편차 배수

        for (int i = period - 1; i < stockCandles.size(); i++) {
            double sum = 0;
            for (int j = i; j > i - period; j--) {
                sum += stockCandles.get(j).getClose(); // 종가를 사용한다고 가정
            }
            double sma = sum / period; // 이동평균
            double sumOfSquaredDifferences = 0;
            for (int j = i; j > i - period; j--) {
                double difference = stockCandles.get(j).getClose() - sma;
                sumOfSquaredDifferences += difference * difference;
            }
            double standardDeviation = Math.sqrt(sumOfSquaredDifferences / period); // 표준편차
            double upperBand = sma + (multiplier * standardDeviation); // 상단 볼린저 밴드
            double lowerBand = sma - (multiplier * standardDeviation); // 하단 볼린저 밴드

            stockCandles.get(i).setBollingerBands((upperBand + lowerBand) / 2); // 볼린저 밴드 값은 중간값으로 설정
        }
    }

    private static void calculateMACD(List<StockCandle> stockCandles) {
        int shortTermPeriod = 12; // 단기 이동평균 주기
        int longTermPeriod = 26; // 장기 이동평균 주기

        // 단기 이동평균 계산
        calculateMovingAverage(stockCandles, shortTermPeriod);
        // 장기 이동평균 계산
        calculateMovingAverage(stockCandles, longTermPeriod);

        // MACD 계산
        for (int i = longTermPeriod - 1; i < stockCandles.size(); i++) {
            double shortTermMA = stockCandles.get(i).getMovingAverage_12(); // 단기 이동평균
            double longTermMA = stockCandles.get(i).getMovingAverage_26(); // 장기 이동평균
            double macd = shortTermMA - longTermMA; // MACD 값

            stockCandles.get(i).setMacd(macd);
        }
    }
}
