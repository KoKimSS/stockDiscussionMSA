package com.example.stockbatch.batch.stockCandle;

import com.example.stockbatch.domain.StockCandle;
import com.example.stockbatch.repository.StockJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
@Slf4j
@RequiredArgsConstructor
public class StockCandleItemWriter implements ItemWriter<List<StockCandle>> {

    private final StockJdbcRepository stockJdbcRepository;

    @Override
    public void write(Chunk<? extends List<StockCandle>> chunk) throws Exception {
        log.info("라이터 수행");
        List<? extends List<StockCandle>> items = chunk.getItems();
        List<StockCandle> list = new ArrayList<>();
        items.forEach(i->list.addAll(i));
        log.info("리스트 사이즈={}",list.size());
        stockJdbcRepository.batchInsertStockCandles(list);
    }
}