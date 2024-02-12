package com.example.stockbatch.batch.stockInfo;


import com.example.stockbatch.batch.stockCandle.StockCandleItemProcessor;
import com.example.stockbatch.batch.stockCandle.StockCandleItemWriter;
import com.example.stockbatch.domain.Stock;
import com.example.stockbatch.domain.StockCandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StockInfoBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final StockInfoItemReader stockInfoItemReader;
    private final StockCandleItemWriter stockItemWriter;
    private final StockCandleItemProcessor stockItemProcessor;

    @Bean
    public Job stockLastInfoJob() {
        return new JobBuilder("stockLastInfo", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stockLastInfoStep())
                .build();
    }

    @Bean
    public Step stockLastInfoStep() {
        return new StepBuilder("stockCandleStep", jobRepository)
                .<Stock, List<StockCandle>>chunk(1000,platformTransactionManager)
                .reader(stockInfoItemReader)
                .processor(stockItemProcessor)
                .writer(stockItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}
