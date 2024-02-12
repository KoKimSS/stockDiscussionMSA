package com.example.stockbatch.batch;

import com.example.stockbatch.domain.Stock;
import com.example.stockbatch.domain.Stock2;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaPageJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private int chunkSize = 10;


    @Bean
    public Job jpaPageJob1(JobRepository jobRepository) {
        return new JobBuilder("jpaPageJob2", jobRepository)
                .start(jpaStep1())
                .build();
    }

    @Bean
    public Step jpaStep1() {
        log.info("step1 실행");
        return new StepBuilder("jpaStep1", jobRepository)
                .<Stock, Stock2>chunk(chunkSize, platformTransactionManager)
                .reader(jpaPageJob1_dbItemReader())
                .processor(jpaPageJob1_processor())
                .writer(jpaPageJob2_printStockWriter())
                .build();
    }

    private ItemProcessor<Stock,Stock2> jpaPageJob1_processor() {
        return Stock ->{
            return Stock2.builder()
                    .code(Stock.getItemCode())
                    .name("NEW_"+Stock.getStockName()).build();
        };
    }

    @Bean
    public JpaPagingItemReader<Stock> jpaPageJob1_dbItemReader() {
        log.info("리더 실행");
        return new JpaPagingItemReaderBuilder<Stock>()
                .name("jpaPageJob1_dbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT s FROM Stock s order by itemCode asc")
                .build();
    }

    public ItemWriter<? super Object> jpaPageJob1_printStockWriter() {
        log.info("라이터 실행");
        return list->{
            for (Object object : list) {
                Stock stock = (Stock) object;
                log.info("라이터 실행 {}",stock.toString());
            }
        };
    }
    public ItemWriter<Stock2> jpaPageJob2_printStockWriter() {
        log.info("라이터 실행");
        return list->{
            for (Stock2 object : list) {
                log.info("라이터 실행 {}",object.toString());
            }
        };
    }
}
