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
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class StockInfoService {
    private static final String KOSDAQ_URL = "https://m.stock.naver.com/api/stocks/marketValue/KOSDAQ";
    private static final String KOSPI_URL = "https://m.stock.naver.com/api/stocks/marketValue/KOSPI";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StockJpaRepository stockJpaRepository;
    private final StockJdbcRepository stockJdbcRepository;
    private final int maxPageSize = 100;


    public List<Stock> getStockInfo() throws IOException, ExecutionException, InterruptedException {
        //전날의 정보는 모두 지운다.
        stockJpaRepository.deleteAllInBatch();

//        CompletableFuture<List<Stock>> kosdaqFuture = CompletableFuture.supplyAsync(() -> {
//            List<Stock> kosdaqStocks = null;
//            try {
//                kosdaqStocks = fetchStockData(KOSDAQ_URL);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            return kosdaqStocks;
//        });
//
//        CompletableFuture<List<Stock>> kospiFuture = CompletableFuture.supplyAsync(() -> {
//            List<Stock> kospiStocks = null;
//            try {
//                kospiStocks = fetchStockData(KOSPI_URL);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            return kospiStocks;
//        });

        List<Stock> stocks = fetchStockData2(KOSPI_URL);
        List<Stock> stocks2 = fetchStockData2(KOSDAQ_URL);
        stocks.addAll(stocks2);


//        List<Stock> kosdaqStocks = kosdaqFuture.get();
//        List<Stock> kospiStocks = kospiFuture.get();
//        System.out.println(kosdaqStocks.size());
//        System.out.println(kospiStocks.size());
//        kosdaqStocks.addAll(kospiStocks);
//        System.out.println("전체 사이즈 "+kosdaqStocks.size());
        System.out.println("전체 사이즈 "+stocks.size());
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
                    System.out.println(page+" 페이지"+ stocks.size());

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

    private List<Stock> fetchStockData2(String urlString) throws IOException {
        int totalCount = getTotalCount(urlString);
        System.out.println(totalCount);

        List<Stock> stocks = new ArrayList<>();
        int totalPages = (totalCount / maxPageSize) + 1;

        ExecutorService executor = Executors.newFixedThreadPool(totalPages);

        CountDownLatch latch = new CountDownLatch(totalPages); // 각 쓰레드의 완료를 기다리기 위한 CountDownLatch

        for (int i = 1; i <= totalPages; i++) {
            int page = i;
            executor.submit(() -> {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    // 작업 수행
                    URL url = new URL(urlString + "?page=" + page + "&pageSize=" + maxPageSize);
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
                    System.out.println("한페이지 사이즈 "+stocksNode.size());
                    String categoryType = rootNode.get("stockListCategoryType").asText();

                    for (JsonNode stockNode : stocksNode) {
                        String tradingValueText = stockNode.get("accumulatedTradingValue").asText();
                        String tradingVolumeText = stockNode.get("accumulatedTradingVolume").asText();
                        String tradingValueString = tradingValueText.equals("-") ? "0" : tradingValueText.replace(",", "");
                        String tradingVolumeString = tradingVolumeText.equals("-") ? "0" : tradingVolumeText.replace(",", "");
                        long accumulatedTradingValue = Long.parseLong(tradingValueString);
                        long accumulatedTradingVolume = Long.parseLong(tradingVolumeString);
                        Stock stock = Stock.builder()
                                .stockName(stockNode.get("stockName").asText())
                                .itemCode(stockNode.get("itemCode").asText())
                                .category(categoryType)
                                .accumulatedTradingValue(accumulatedTradingValue)
                                .accumulatedTradingVolume(accumulatedTradingVolume)
                                .fluctuationsRatio(stockNode.get("fluctuationsRatio").asDouble())
                                .build();
                        stocks.add(stock);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 쓰레드 완료 시 CountDownLatch 감소
                    latch.countDown();

                    // 리소스 정리
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (connection != null) {
                            connection.disconnect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        try {
            latch.await(); // 모든 쓰레드가 완료될 때까지 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown(); // 작업이 완료되면 ExecutorService 종료
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