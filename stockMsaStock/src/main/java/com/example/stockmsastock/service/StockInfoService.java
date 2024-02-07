package com.example.stockmsastock.service;

import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.repository.StockJpaRepository;
import com.example.stockmsastock.web.dto.ItemsDTO;
import com.example.stockmsastock.web.dto.request.GetStockInfoRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInfoService {
    private static final String API_URL = "https://apis.data.go.kr/1160100/service/GetKrxListedInfoService";
    private final StockJpaRepository stockJpaRepository;
    @Value("${krx.api.Encoding.key}")
    private String serviceKey;
    public List<Stock> callApi(GetStockInfoRequestDto requestDto) {
        List<Stock> stocks = new ArrayList<>();
        HttpURLConnection conn = null;
        JSONObject result = null;
        try {
            StringBuilder urlStringBuilder = new StringBuilder(API_URL);
            URL url = getUrl(requestDto, urlStringBuilder);

            conn = (HttpURLConnection) url.openConnection();
            // type의 경우 POST, GET, PUT, DELETE 가능
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 보내고 결과값 받기
            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    sb.append(line);
                }
                result = new JSONObject(sb.toString());
                stocks = parseJsonToStockList(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stockJpaRepository.saveAll(stocks);
        return stocks;
    }

    private URL getUrl(GetStockInfoRequestDto requestDto, StringBuilder urlStringBuilder) throws UnsupportedEncodingException, MalformedURLException {
        urlStringBuilder.append("/getItemInfo");
        urlStringBuilder.append("?serviceKey=").append(serviceKey);
        urlStringBuilder.append("&numOfRows=").append(requestDto.getNumOfRows());
        urlStringBuilder.append("&pageNo=").append(requestDto.getPageNo());

        // resultType 추가
        if (requestDto.getResultType() != null) {
            urlStringBuilder.append("&resultType=").append(URLEncoder.encode(requestDto.getResultType(), "UTF-8"));
        }

        // basDt 추가
        if (requestDto.getBasDt() != null) {
            urlStringBuilder.append("&basDt=").append(URLEncoder.encode(requestDto.getBasDt(), "UTF-8"));
        }

        // beginBasDt 추가
        if (requestDto.getBeginBasDt() != null) {
            urlStringBuilder.append("&beginBasDt=").append(URLEncoder.encode(requestDto.getBeginBasDt(), "UTF-8"));
        }

        // endBasDt 추가
        if (requestDto.getEndBasDt() != null) {
            urlStringBuilder.append("&endBasDt=").append(URLEncoder.encode(requestDto.getEndBasDt(), "UTF-8"));
        }

        // likeBasDt 추가
        if (requestDto.getLikeBasDt() != null) {
            urlStringBuilder.append("&likeBasDt=").append(URLEncoder.encode(requestDto.getLikeBasDt(), "UTF-8"));
        }

        // likeSrtnCd 추가
        if (requestDto.getLikeSrtnCd() != null) {
            urlStringBuilder.append("&likeSrtnCd=").append(URLEncoder.encode(requestDto.getLikeSrtnCd(), "UTF-8"));
        }

        // isinCd 추가
        if (requestDto.getIsinCd() != null) {
            urlStringBuilder.append("&isinCd=").append(URLEncoder.encode(requestDto.getIsinCd(), "UTF-8"));
        }

        // likeIsinCd 추가
        if (requestDto.getLikeIsinCd() != null) {
            urlStringBuilder.append("&likeIsinCd=").append(URLEncoder.encode(requestDto.getLikeIsinCd(), "UTF-8"));
        }

        // itmsNm 추가
        if (requestDto.getItmsNm() != null) {
            urlStringBuilder.append("&itmsNm=").append(URLEncoder.encode(requestDto.getItmsNm(), "UTF-8"));
        }

        // likeItmsNm 추가
        if (requestDto.getLikeItmsNm() != null) {
            urlStringBuilder.append("&likeItmsNm=").append(URLEncoder.encode(requestDto.getLikeItmsNm(), "UTF-8"));
        }

        // crno 추가
        if (requestDto.getCrno() != null) {
            urlStringBuilder.append("&crno=").append(URLEncoder.encode(requestDto.getCrno(), "UTF-8"));
        }

        // corpNm 추가
        if (requestDto.getCorpNm() != null) {
            urlStringBuilder.append("&corpNm=").append(URLEncoder.encode(requestDto.getCorpNm(), "UTF-8"));
        }

        // likeCorpNm 추가
        if (requestDto.getLikeCorpNm() != null) {
            urlStringBuilder.append("&likeCorpNm=").append(URLEncoder.encode(requestDto.getLikeCorpNm(), "UTF-8"));
        }

        URL url = new URL(urlStringBuilder.toString());
        return url;
    }

    private static List<Stock> parseJsonToStockList(JSONObject jsonObject) {
        List<Stock> stockList = new ArrayList<>();
        try {
            JSONObject itemsObject = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items");
            JSONArray itemArray = itemsObject.getJSONArray("item");

            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject itemObject = itemArray.getJSONObject(i);
                Stock stock = Stock.builder()
                        .basDt(itemObject.getString("basDt"))
                        .srtnCd(itemObject.getString("srtnCd"))
                        .isinCd(itemObject.getString("isinCd"))
                        .mrktCtg(itemObject.getString("mrktCtg"))
                        .itmsNm(itemObject.getString("itmsNm"))
                        .crno(itemObject.getString("crno"))
                        .corpNm(itemObject.getString("corpNm"))
                        .build();
                stockList.add(stock);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stockList;
    }
}