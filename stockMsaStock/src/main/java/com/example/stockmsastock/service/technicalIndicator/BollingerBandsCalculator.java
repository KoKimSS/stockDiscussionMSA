package com.example.stockmsastock.service.technicalIndicator;

import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class BollingerBandsCalculator implements TechnicalIndicator{
    private final int period;
    private final double multiplier;

    @Override
    public List<Integer> calculate(List<Integer> priceData) {
        // 볼린저 밴드 계산 로직을 구현합니다.
        // 여기에는 주가 데이터를 이용하여 상단 밴드, 중간 밴드, 하단 밴드를 계산하는 등의 작업이 포함됩니다.
        // 각 밴드 값들을 리스트로 반환합니다.
        // 여기에서는 간단한 로직을 예시로 제시하겠습니다.
        // 실제 사용하는 경우에는 해당 지표에 대한 정확한 계산식을 적용해야 합니다.
        // 예시로 20일 이동평균선을 기준으로 볼린저 밴드를 계산하는 방식입니다.

        List<Integer> movingAverages = new MovingAverage(period).calculate(priceData);
        Integer middleBand = movingAverages.get(movingAverages.size() - 1);
        Double standardDeviation = calculateStandardDeviation(priceData, middleBand);
        Integer upperBand = (int) (middleBand + multiplier * standardDeviation);
        Integer lowerBand = (int) (middleBand - multiplier * standardDeviation);

        return List.of(upperBand, middleBand, lowerBand);
    }

    private Double calculateStandardDeviation(List<Integer> priceData, Integer middleBand) {
        Double sumOfSquares = (double) 0;
        for (Integer price : priceData) {
            sumOfSquares += Math.pow(price - middleBand, 2);
        }
        Double variance = sumOfSquares / priceData.size();
        return Math.sqrt(variance);
    }
}
