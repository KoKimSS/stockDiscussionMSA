package com.example.stockmsastock.service.technicalIndicator;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MovingAverage implements TechnicalIndicator{
    private final int period;


    @Override
    public List<Integer> calculate(List<Integer> priceData) {
        // 이동평균선 계산 로직을 여기에 구현합니다.
        // 예를 들어, priceData 리스트의 마지막 n개 항목을 이용하여 이동평균을 계산할 수 있습니다.
        // 이 예제에서는 간단히 priceData 리스트의 마지막 period 개 항목의 평균을 반환합니다.
        List<Integer> movingAverage = new ArrayList<>();
        if (priceData.size() >= period) {
            Integer sum = 0;
            for (int i = priceData.size() - period; i < priceData.size(); i++) {
                sum += priceData.get(i);
            }
            Integer average = sum / period;
            movingAverage.add(average);
        }
        return movingAverage;
    }
}
