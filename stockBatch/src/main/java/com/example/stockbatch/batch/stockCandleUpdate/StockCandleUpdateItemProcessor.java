package com.example.stockbatch.batch.stockCandleUpdate;

import com.example.stockbatch.api.StockCandleService;
import com.example.stockbatch.domain.Stock;
import com.example.stockbatch.domain.StockCandle;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class StockCandleUpdateItemProcessor implements ItemProcessor<Stock, List<StockCandle>> {
    private final StockCandleService stockCandleService;
    @Override
    public List<StockCandle> process(Stock stock) throws Exception {
        return stockCandleService.fetchNextStockCandleForUpdate(stock.getItemCode(),1);
    }
}