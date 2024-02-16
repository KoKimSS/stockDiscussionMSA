package com.example.stockmsastock.web.controller;

import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.domain.stock.StockPrice;
import com.example.stockmsastock.service.StockService;
import com.example.stockmsastock.web.dto.request.FindByItemCodeRequestDto;
import com.example.stockmsastock.web.dto.request.FindByNameRequestDto;
import com.example.stockmsastock.web.dto.request.GetPriceByCodeDto;
import com.example.stockmsastock.web.dto.request.GetStockPageOrderByRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/stock/")
public class StockController {

    private final StockService stockService;

    @PostMapping
    @RequestMapping("find-by-name")
    ResponseEntity<List<Stock>> findByName(
            @RequestBody FindByNameRequestDto requestDto
    ){
        List<Stock> stocks = stockService.findByName(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @PostMapping
    @RequestMapping("find-by-itemCode")
    ResponseEntity<Stock> findByItemCode(
            @RequestBody FindByItemCodeRequestDto requestDto
    ){
        Stock stock = stockService.findByItemCode(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }


    @PostMapping
    @RequestMapping("find-page-orderBy")
    ResponseEntity<Page<Stock>> getPageOrderBy(
            @RequestBody GetStockPageOrderByRequestDto requestDto
    ) {
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        Page<Stock> pageOrderBy = stockService.getPageOrderBy(requestDto.getSortBy(), requestDto.getSortOrder(), pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pageOrderBy);
    }
}
