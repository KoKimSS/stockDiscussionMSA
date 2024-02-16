package com.example.stockmsastock.service.technicalIndicator;

import java.util.List;
import java.util.function.Function;

public class TechnicalIndicatorCalculator {

    // 기술 지표 계산을 위한 함수형 인터페이스 정의
    @FunctionalInterface
    interface IndicatorCalculator {
        List<Integer> calculate(List<Integer> priceData);
    }

    // 기술 지표 계산 메소드
    public static List<Integer> calculateTechnicalIndicator(List<Integer> priceData, IndicatorCalculator calculator) {
        return calculator.calculate(priceData);
    }

    public static void main(String[] args) {
        // 예시 데이터
        List<Integer> priceData = List.of(10000, 9000, 8000, 9000, 10000, 11000,
                10000, 9000, 8000, 9000, 10000, 11000,
                10000, 9000, 8000, 9000, 10000, 11000
                );

        // Bollinger Bands 계산
        IndicatorCalculator bollingerBandsCalculator = price -> {
            BollingerBandsCalculator bollingerBands = new BollingerBandsCalculator(3, 2.0); // 적절한 값을 넣어주세요
            return bollingerBands.calculate(price);
        };

        // MACD 계산
        IndicatorCalculator macdCalculator = price -> {
            Macd macd = new Macd(12, 26, 9); // 적절한 값을 넣어주세요
            return macd.calculate(price);
        };

        // Moving Average 계산
        IndicatorCalculator movingAverageCalculator = price -> {
            MovingAverage movingAverage = new MovingAverage(3); // 적절한 값을 넣어주세요
            return movingAverage.calculate(price);
        };

        // 각각의 기술 지표 계산
        List<Integer> bollingerBandsResult = calculateTechnicalIndicator(priceData, bollingerBandsCalculator);
        List<Integer> macdResult = calculateTechnicalIndicator(priceData, macdCalculator);
        List<Integer> movingAverageResult = calculateTechnicalIndicator(priceData, movingAverageCalculator);

        // 결과 출력
        System.out.println("Bollinger Bands: " + bollingerBandsResult);
        System.out.println("MACD: " + macdResult);
        System.out.println("Moving Average: " + movingAverageResult);
    }
}