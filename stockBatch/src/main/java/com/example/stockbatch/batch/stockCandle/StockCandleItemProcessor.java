package com.example.stockbatch.batch.stockCandle;

import com.example.stockbatch.api.StockCandleService;
import com.example.stockbatch.domain.Stock;
import com.example.stockbatch.domain.StockCandle;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class StockCandleItemProcessor implements ItemProcessor<Stock, List<StockCandle>> {

    private final StockCandleService stockCandleService;
    @Value("${stock.candle.count}") // 환경 변수로부터 값을 읽어옴
    private int count;
    @Override
    public List<StockCandle> process(Stock stock) throws Exception {
        List<StockCandle> stockCandles = stockCandleService.fetchNextStockCandle(stock.getItemCode(), count);
//        CalculateIndicators.calculateIndicators(stockCandles);
        return stockCandles;
    }
}