package com.example.stockmsastock.service.technicalIndicator;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Macd implements TechnicalIndicator{
    private final int shortPeriod;
    private final int longPeriod;
    private final int signalPeriod;


    @Override
    public List<Integer> calculate(List<Integer> priceData) {
        List<Integer> macdValues = new ArrayList<>();
        if (priceData.size() >= longPeriod) {
            List<Integer> shortEMA = calculateEMA(priceData, shortPeriod);
            List<Integer> longEMA = calculateEMA(priceData, longPeriod);

            // MACD Line 계산
            List<Integer> macdLine = new ArrayList<>();
            for (int i = 0; i < shortEMA.size(); i++) {
                macdLine.add(shortEMA.get(i) - longEMA.get(i));
            }

            // Signal Line 계산
            List<Integer> signalLine = calculateEMA(macdLine, signalPeriod);

            // MACD Histogram 계산
            for (int i = 0; i < macdLine.size(); i++) {
                macdValues.add(macdLine.get(i) - signalLine.get(i));
            }
        }
        return macdValues;
    }

    private List<Integer> calculateEMA(List<Integer> priceData, int period) {
        List<Integer> emaValues = new ArrayList<>();
        Integer multiplier = (int) (2.0 / (period + 1));
        Integer ema = 0;

        // 처음 EMA 값은 SMA 값으로 시작
        for (int i = 0; i < period; i++) {
            ema += priceData.get(i);
        }
        ema /= period;
        emaValues.add(ema);

        // EMA 계산
        for (int i = period; i < priceData.size(); i++) {
            ema = (priceData.get(i) - ema) * multiplier + ema;
            emaValues.add(ema);
        }

        return emaValues;
    }
}
