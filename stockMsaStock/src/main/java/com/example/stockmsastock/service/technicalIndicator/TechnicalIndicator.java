package com.example.stockmsastock.service.technicalIndicator;

import java.util.List;

public interface TechnicalIndicator {
    List<Integer> calculate(List<Integer> priceData);
}
