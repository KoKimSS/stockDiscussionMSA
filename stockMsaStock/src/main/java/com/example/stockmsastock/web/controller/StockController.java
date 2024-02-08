package com.example.stockmsastock.web.controller;

import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.domain.stock.StockPrice;
import com.example.stockmsastock.service.StockPriceInfoService;
import com.example.stockmsastock.service.StockService;
import com.example.stockmsastock.web.dto.request.FindByNameRequestDto;
import com.example.stockmsastock.web.dto.request.GetPriceByCodeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService stockService;
    private final StockPriceInfoService stockPriceInfoService;

    @PostMapping
    @RequestMapping("find-by-name")
    ResponseEntity<List<Stock>> findByName(
            @RequestBody FindByNameRequestDto requestDto
    ){
        List<Stock> stocks = stockService.findByName(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @PostMapping
    @RequestMapping("get-stock-price")
    ResponseEntity<StockPrice> getByCode(
            @RequestBody GetPriceByCodeDto requestDto
    ) throws MalformedURLException, JsonProcessingException {
        StockPrice stockPrice = stockPriceInfoService.getStockPrice(requestDto.getItemCode());
        return ResponseEntity.status(HttpStatus.OK).body(stockPrice);
    }
}
