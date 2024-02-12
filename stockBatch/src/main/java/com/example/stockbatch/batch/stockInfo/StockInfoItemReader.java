package com.example.stockbatch.batch.stockInfo;

import com.example.stockbatch.api.StockInfoService;
import com.example.stockbatch.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class StockInfoItemReader implements ItemReader<List<Stock>> {

    private final StockInfoService stockInfoService;
    private boolean alreadyRead = false;

    @Override
    public List<Stock> read() throws Exception {
        if (!alreadyRead) {
            alreadyRead = true;
            return stockInfoService.getStockInfo();
        } else {
            return null; // 이후 실행에서 더 이상 읽을 데이터가 없음을 알리기 위해 null 반환
        }
    }
}