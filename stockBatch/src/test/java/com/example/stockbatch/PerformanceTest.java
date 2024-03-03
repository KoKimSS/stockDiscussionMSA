package com.example.stockbatch;

import com.example.stockbatch.domain.StockCandle;
import com.example.stockbatch.repository.StockCandleJpaRepository;
import com.example.stockbatch.repository.StockJdbcRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PerformanceTest {
    private static List<StockCandle> stockCandleList;
    @Autowired
    private StockCandleJpaRepository stockCandleJpaRepository;
    @Autowired
    private StockJdbcRepository stockJdbcRepository;

    @BeforeAll
    static void before() {
        stockCandleList = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            stockCandleList.add(
                    StockCandle.builder()
                            .low(10000)
                            .open(10000)
                            .close(10000)
                            .volume(10000)
                            .high(10000)
                            .date(LocalDate.now())
                            .code("123456")
                            .build()
            );
        }
    }

    @DisplayName("JPA saveAll 성능 테스트")
    @Test
    public void testSaveAllMethod() throws Exception {
        //given
        long startTime = System.currentTimeMillis();

        //when
        stockCandleJpaRepository.saveAll(stockCandleList);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("JPA saveAll 시간" + (endTime - startTime) / 1000 + "초");
    }

    @DisplayName("JDBC batchInsert 성능 테스트")
    @Test
    public void testBatchInsertMethod() throws Exception {
        //given
        long startTime = System.currentTimeMillis();

        //when
        stockJdbcRepository.batchInsertStockCandles(stockCandleList);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("Jdbc batchInsert 시간" + (endTime - startTime) / 1000);
    }
}
