package com.example.stockmsastock.web.controller;

import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.service.StockInfoService;
import com.example.stockmsastock.web.dto.ItemsDTO;
import com.example.stockmsastock.web.dto.request.GetStockInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetStockController {

    private final StockInfoService stockInfoService;
    @GetMapping("/get-stock-infos")
    ResponseEntity getStockInfos(){
        GetStockInfoRequestDto requestDto = GetStockInfoRequestDto.builder().numOfRows(10)
                .pageNo(1)
                .resultType("json")
                .basDt("20240206")
                .build();
        List<Stock> stocks = stockInfoService.callApi(requestDto);
        stocks.forEach(stock -> System.out.println(stock.getItmsNm()));
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }
}
