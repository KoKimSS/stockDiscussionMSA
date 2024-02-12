package com.example.stockbatch.batch.stockInfo;

import com.example.stockbatch.domain.Stock;
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
public class StockInfoItemWriter implements ItemWriter<List<Stock>> {

    private final StockJdbcRepository stockJdbcRepository;

    @Override
    public void write(Chunk<? extends List<Stock>> chunk) throws Exception {
        log.info("라이터 수행");
        chunk.getItems().forEach(stocks -> {
            log.info("스톡 사이즈 {}",stocks.size());
            stockJdbcRepository.batchInsertStocks(stocks);
        });
    }
}