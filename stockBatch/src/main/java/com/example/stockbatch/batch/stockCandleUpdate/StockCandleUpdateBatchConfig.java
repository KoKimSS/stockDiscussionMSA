package com.example.stockbatch.batch.stockCandleUpdate;


import com.example.stockbatch.batch.stockCandle.StockCandleItemProcessor;
import com.example.stockbatch.batch.stockCandle.StockCandleItemReader;
import com.example.stockbatch.batch.stockCandle.StockCandleItemWriter;
import com.example.stockbatch.domain.Stock;
import com.example.stockbatch.domain.StockCandle;
import com.example.stockbatch.repository.StockJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StockCandleUpdateBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final StockJdbcRepository stockJdbcRepository;
    private final StockCandleUpdateItemReader stockCandleUpdateItemReader;
    private final StockCandleUpdateItemProcessor stockCandleUpdateItemProcessor;
    private final StockCandleUpdateItemWriter stockCandleUpdateItemWriter;

    @Bean
    public Job stockCandleUpdateJob() {
        return new JobBuilder("stockCandleUpdateJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(deleteOldCandleStep())
                .next(updateCurrentCandle())
                .build();
    }

    @Bean
    public Step deleteOldCandleStep() {
        return new StepBuilder("deleteOldCandleStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<String> stockCodesWithOverThousand = stockJdbcRepository.getStockCodesWithOverThousand();
                    stockJdbcRepository.cleanupStockData(stockCodesWithOverThousand);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager).build();
    }

    @Bean
    public Step updateCurrentCandle() {
        return new StepBuilder("updateCurrentCandleStep", jobRepository)
                .<Stock, List<StockCandle>>chunk(1000,platformTransactionManager)
                .reader(stockCandleUpdateItemReader)
                .processor(stockCandleUpdateItemProcessor)
                .writer(stockCandleUpdateItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}
