package com.example.stockbatch.batch;

import com.example.stockbatch.domain.StockCandle;
import com.example.stockbatch.repository.StockCandleJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockItemWriter implements ItemWriter<List<StockCandle>> {

    private final StockCandleJdbcRepository stockRepository; // Stock 엔티티에 접근하는 Spring Data JPA Repository

    @Override
    public void write(Chunk<? extends List<StockCandle>> chunk) throws Exception {
        for (List<StockCandle> stockCandleList : chunk) {
            stockRepository.batchInsertStocks(stockCandleList);
        }
    }
}
