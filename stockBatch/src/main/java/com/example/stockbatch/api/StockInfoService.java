package com.example.stockbatch.api;

import com.example.stockbatch.domain.Stock;
import com.example.stockbatch.repository.StockJdbcRepository;
import com.example.stockbatch.repository.StockJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StockInfoService {
    private static final String KOSDAQ_URL = "https://m.stock.naver.com/api/stocks/marketValue/KOSDAQ?page={page}&pageSize={pageSize}";
    private static final String KOSPI_URL = "https://m.stock.naver.com/api/stocks/marketValue/KOSPI?page={page}&pageSize={pageSize}";
    private final ObjectMapper objectMapper;
    private final StockJpaRepository stockJpaRepository;
    private final StockJdbcRepository stockJdbcRepository;
    private final int maxPageSize = 100;


    public List<Stock> getStockInfo() throws IOException {
        long startTime = System.currentTimeMillis();
        //전날의 정보는 모두 지운다.
        stockJpaRepository.deleteAllInBatch();

        List<Stock> kosdaqStocks = getStocksByCategory(KOSDAQ_URL);
        stockJpaRepository.saveAll(kosdaqStocks);
        List<Stock> kospiStocks = getStocksByCategory(KOSPI_URL);
        stockJpaRepository.saveAll(kospiStocks);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("실행 시간: " + duration + "밀리초");
        return kosdaqStocks;
    }

    public List<Stock> getStockInfo2() throws JsonProcessingException, MalformedURLException {
        long startTime = System.currentTimeMillis();
        //전날의 정보는 모두 지운다.
        stockJpaRepository.deleteAllInBatch();

        CompletableFuture<List<Stock>> kosdaqFuture = CompletableFuture.supplyAsync(() -> {
            long kosdaqStartTime = System.currentTimeMillis();
            List<Stock> kosdaqStocks = null;
            try {
                kosdaqStocks = fetchStockData(KOSDAQ_URL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            long kosdaqEndTime = System.currentTimeMillis();
            System.out.println("코스닥 작업 시작 시간: " + kosdaqStartTime + ", 종료 시간: " + kosdaqEndTime);
            return kosdaqStocks;
        });

        CompletableFuture<List<Stock>> kospiFuture = CompletableFuture.supplyAsync(() -> {
            long kospiStartTime = System.currentTimeMillis();
            List<Stock> kospiStocks = null;
            try {
                kospiStocks = fetchStockData(KOSPI_URL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            long kospiEndTime = System.currentTimeMillis();
            System.out.println("코스피 작업 시작 시간: " + kospiStartTime + ", 종료 시간: " + kospiEndTime);
            return kospiStocks;
        });

        List<Stock> kosdaqStocks = kosdaqFuture.join();
        List<Stock> kospiStocks = kospiFuture.join();
        kosdaqStocks.addAll(kospiStocks);
        System.out.println("addAll");
        stockJdbcRepository.batchInsertStocks(kosdaqStocks);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("실행 시간: " + duration + "밀리초");
        return kosdaqStocks;
    }


    private List<Stock> getStocksByCategory(String urlString) throws IOException {
        List<Stock> stocks = new ArrayList<>();
        int totalCount = getTotalCount(urlString);
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            for (int i = 1; i <= totalCount / 100 + 1; i++) {
                URL url = new URL(urlString + "?page=" + i + "&pageSize=" + maxPageSize);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line);
                }

                JsonNode rootNode = objectMapper.readTree(responseBody.toString());
                JsonNode stocksNode = rootNode.get("stocks");
                for (JsonNode stockNode : stocksNode) {
                    Stock stock = Stock.builder()
                            .stockName(stockNode.get("stockName").asText())
                            .itemCode(stockNode.get("itemCode").asText())
                            .category("KOSDAQ")
                            .build();
                    stocks.add(stock);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return stocks;
    }

    private List<Stock> fetchStockData(String urlString) throws IOException {
        int totalCount = getTotalCount(urlString);
        System.out.println(totalCount);

        List<Stock> stocks = new ArrayList<>();
        int totalPages = (totalCount / maxPageSize) + 1;
        ExecutorService executor = Executors.newFixedThreadPool(totalPages);

        try {
            for (int i = 1; i <= totalPages; i++) {
                int page = i;
                executor.submit(() -> {
                    HttpURLConnection connection = null;
                    BufferedReader reader = null;

                    URL url = new URL(urlString + "?page=" + page + "&pageSize=" + maxPageSize);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBody = new StringBuilder();
                    String line;
                    while (true) {
                        try {
                            if ((line = reader.readLine()) == null) break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        responseBody.append(line);
                    }

                    JsonNode rootNode = objectMapper.readTree(responseBody.toString());
                    JsonNode stocksNode = rootNode.get("stocks");
                    for (JsonNode stockNode : stocksNode) {
                        Stock stock = Stock.builder()
                                .stockName(stockNode.get("stockName").asText())
                                .itemCode(stockNode.get("itemCode").asText())
                                .category("KOSDAQ")
                                .build();
                        stocks.add(stock);
                    }

                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return stocks;
    }

    private int getTotalCount(String urlString) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString + "?page=1&pageSize=1");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }

            JsonNode rootNode = objectMapper.readTree(responseBody.toString());
            return rootNode.get("totalCount").asInt();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}