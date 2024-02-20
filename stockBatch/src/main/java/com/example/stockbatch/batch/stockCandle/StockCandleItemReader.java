package com.example.stockbatch.batch.stockCandle;

import com.example.stockbatch.domain.Stock;
import com.example.stockbatch.repository.StockCandleJpaRepository;
import com.example.stockbatch.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class StockCandleItemReader implements ItemReader<Stock> {

    private final StockJpaRepository stockJpaRepository;
    private final StockCandleJpaRepository stockCandleJpaRepository;
    private Iterator<Stock> stockIterator;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        List<Stock> stocks = stockJpaRepository.findAll();
        this.stockIterator = stocks.iterator();
    }

    @Override
    public Stock read() throws Exception {
        return stockIterator.hasNext() ? stockIterator.next() : null;
    }
}