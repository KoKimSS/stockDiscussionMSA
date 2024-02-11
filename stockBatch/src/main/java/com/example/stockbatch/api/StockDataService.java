package com.example.stockbatch.api;

import com.example.stockbatch.domain.StockCandle;
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

@Service
public class StockDataService {
    public StockCandle fetchNextStockCandle(String symbol) throws Exception {
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

        // 여기서부터는 한 번에 하나의 StockCandle을 반환하는 방식으로 변경
        for (int i = 0; i < chartDataList.getLength(); i++) {
            Node chartDataNode = chartDataList.item(i);
            if (chartDataNode.getNodeType() == Node.ELEMENT_NODE) {
                Element chartDataElement = (Element) chartDataNode;
                NodeList itemNodeList = chartDataElement.getElementsByTagName("item");

                for (int j = 0; j < itemNodeList.getLength(); j++) {
                    Node itemNode = itemNodeList.item(j);
                    if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element itemElement = (Element) itemNode;
                        String[] data = itemElement.getAttribute("data").split("\\|");
                        String date = data[0];
                        int open = Integer.parseInt(data[1]);
                        int high = Integer.parseInt(data[2]);
                        int low = Integer.parseInt(data[3]);
                        int close = Integer.parseInt(data[4]);
                        int volume = Integer.parseInt(data[5]);

                        return StockCandle.builder()
                                .code(symbol)
                                .date(date)
                                .open(open)
                                .high(high)
                                .low(low)
                                .close(close)
                                .volume(volume)
                                .build();
                    }
                }
            }
        }
        // 데이터를 모두 읽었을 때는 null 반환
        return null;
    }
}
