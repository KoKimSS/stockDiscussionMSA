package com.example.stockbatch.batch;

import com.example.stockbatch.api.StockDataService;
import com.example.stockbatch.domain.StockCandle;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Component
@StepScope
@RequiredArgsConstructor
public class ParallelStockItemReader implements ItemReader<StockCandle> {

    private final StockDataService stockDataService; // 실제 데이터를 가져오는 서비스
    private List<String> stockCodes = List.of("435870","442130","465320","161570"); // 종목코드 리스트
    private int currentIndex = 0; // 현재 인덱스
    private final SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(); // 병렬 처리를 위한 TaskExecutor

    @Override
    public StockCandle read() throws Exception {
        if (currentIndex < stockCodes.size()) {
            // 종목코드 리스트에서 다음 종목코드를 가져옴
            String stockCode = stockCodes.get(currentIndex);
            currentIndex++;

            // 병렬로 주식 데이터를 읽어오는 작업을 실행
            CompletableFuture<StockCandle> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return stockDataService.fetchNextStockCandle(stockCode);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, taskExecutor);
            // 병렬 작업 완료를 기다림
            return future.get();
        } else {
            // 더 이상 데이터가 없는 경우 처리
            return null;
        }
    }

    public void setStockCodes(List<String> stockCodes) {
        this.stockCodes = stockCodes;
    }
}
