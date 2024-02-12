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
import java.net.ProtocolException;
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
    private static final String KOSDAQ_URL = "https://m.stock.naver.com/api/stocks/marketValue/KOSDAQ";
    private static final String KOSPI_URL = "https://m.stock.naver.com/api/stocks/marketValue/KOSPI";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StockJpaRepository stockJpaRepository;
    private final StockJdbcRepository stockJdbcRepository;
    private final int maxPageSize = 100;


    public List<Stock> getStockInfo() throws JsonProcessingException, MalformedURLException {
        //전날의 정보는 모두 지운다.
        stockJpaRepository.deleteAllInBatch();

        CompletableFuture<List<Stock>> kosdaqFuture = CompletableFuture.supplyAsync(() -> {
            List<Stock> kosdaqStocks = null;
            try {
                kosdaqStocks = fetchStockData(KOSDAQ_URL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return kosdaqStocks;
        });

        CompletableFuture<List<Stock>> kospiFuture = CompletableFuture.supplyAsync(() -> {
            List<Stock> kospiStocks = null;
            try {
                kospiStocks = fetchStockData(KOSPI_URL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return kospiStocks;
        });

        List<Stock> kosdaqStocks = kosdaqFuture.join();
        List<Stock> kospiStocks = kospiFuture.join();
        kosdaqStocks.addAll(kospiStocks);
        return kosdaqStocks;
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

                    URL url = null;
                    try {
                        url = new URL(urlString + "?page=" + page + "&pageSize=" + maxPageSize);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        connection.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

                    JsonNode rootNode = null;
                    try {
                        rootNode = objectMapper.readTree(responseBody.toString());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    JsonNode stocksNode = rootNode.get("stocks");
                    String categoryType = rootNode.get("stockListCategoryType").asText();
                    for (JsonNode stockNode : stocksNode) {
                        Stock stock = Stock.builder()
                                .stockName(stockNode.get("stockName").asText())
                                .itemCode(stockNode.get("itemCode").asText())
                                .category(categoryType)
                                .accumulatedTradingValue(Long.parseLong(stockNode.get("accumulatedTradingValue").asText().replace(",", "")))
                                .accumulatedTradingVolume(Long.parseLong(stockNode.get("accumulatedTradingVolume").asText().replace(",", "")))
                                .fluctuationsRatio(stockNode.get("fluctuationsRatio").asDouble())
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