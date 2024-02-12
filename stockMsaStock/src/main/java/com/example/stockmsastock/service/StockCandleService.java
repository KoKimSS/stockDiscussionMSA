package com.example.stockmsastock.service;

import com.example.stockmsastock.domain.StockCandle;
import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.repository.StockCandleJdbcRepository;
import com.example.stockmsastock.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class StockCandleService {

    private final StockJpaRepository stockJpaRepository;
    private final StockCandleJdbcRepository stockCandleJdbcRepository;

    public void getStockCandles() throws Exception {
        List<String> codes = stockJpaRepository.findAll().stream()
                .map(Stock::getItemCode).toList();

        // 쓰레드 풀 생성
        ExecutorService executor = Executors.newFixedThreadPool(20); // 여기서는 최대 10개의 스레드로 설정

        // Future 리스트 생성
        List<Future<List<StockCandle>>> futures = new ArrayList<>();

        // 각 코드에 대해 fetchNextStockCandle()을 호출하고 Future에 추가
        for(String code : codes){
            Callable<List<StockCandle>> task = () -> fetchNextStockCandle(code);
            Future<List<StockCandle>> future = executor.submit(task);
            futures.add(future);
        }


        // 각 Future가 완료될 때마다 결과를 데이터베이스에 즉시 삽입
        for (Future<List<StockCandle>> future : futures) {
            executor.submit(() -> {
                try {
                    List<StockCandle> stockCandles = future.get(); // 결과 가져오기 (블록킹)
                    System.out.println("스톡사이즈 = " + stockCandles.size());
                    stockCandleJdbcRepository.batchInsertStocks(stockCandles); // 데이터베이스에 추가
                    long costTime = ApplicationStartup.startTime - System.currentTimeMillis();
                    System.out.println("수행시간"+costTime);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace(); // 예외 처리
                }
            });
        }
    }


    public List<StockCandle> fetchNextStockCandle(String symbol) throws Exception {
        String urlString = "https://fchart.stock.naver.com/sise.nhn?symbol=" + symbol + "&timeframe=day&count=1000&requestType=0";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        InputStream inputStream = conn.getInputStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        Element root = document.getDocumentElement();
        NodeList chartDataList = root.getElementsByTagName("chartdata");

        List<StockCandle> stockCandles = new ArrayList<>();

        // 여기서부터는 한 번에 하나의 StockCandle을 반환하는 방식으로 변경
        for (int i = 0; i < chartDataList.getLength(); i++) {
            Node chartDataNode = chartDataList.item(i);
            if (chartDataNode.getNodeType() == Node.ELEMENT_NODE) {
                Element chartDataElement = (Element) chartDataNode;
                NodeList itemNodeList = chartDataElement.getElementsByTagName("item");

                for (int j = 0; j < itemNodeList.getLength(); j++) {
                    Node itemNode = itemNodeList.item(j);
                    Element itemElement = (Element) itemNode;
                    String[] data = itemElement.getAttribute("data").split("\\|");
                    String date = data[0];
                    int open = Integer.parseInt(data[1]);
                    int high = Integer.parseInt(data[2]);
                    int low = Integer.parseInt(data[3]);
                    int close = Integer.parseInt(data[4]);
                    int volume = Integer.parseInt(data[5]);

                    stockCandles.add(StockCandle.builder()
                            .code(symbol)
                            .date(date)
                            .open(open)
                            .high(high)
                            .low(low)
                            .close(close)
                            .volume(volume)
                            .build());
                }
            }
        }
        // 데이터를 모두 읽었을 때는 null 반환
        return stockCandles;
    }
}
