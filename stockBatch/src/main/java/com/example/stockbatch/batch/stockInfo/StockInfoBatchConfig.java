package com.example.stockbatch.batch.stockInfo;


import com.example.stockbatch.domain.Stock;
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
    private final StockInfoItemWriter stockItemWriter;

    @Bean
    public Job stockInfoJob() {
        return new JobBuilder("stockInfoJob", jobRepository)
                .start(stockInfoStep())
                .build();
    }

    @Bean
    public Step stockInfoStep() {
        return new StepBuilder("stockInfoStep", jobRepository)
                .<List<Stock>, List<Stock>>chunk(1,platformTransactionManager)
                .reader(stockInfoItemReader)
                .writer(stockItemWriter)
                .build();
    }
}
