package com.example.stockmsastock.service;

import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.repository.StockJpaRepository;
import com.example.stockmsastock.repository.StockRepository;
import com.example.stockmsastock.web.dto.request.FindByItemCodeRequestDto;
import com.example.stockmsastock.web.dto.request.FindByNameRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockJpaRepository stockJpaRepository;
    private final StockRepository stockRepository;

    public List<Stock> findByName(FindByNameRequestDto findByNameRequestDto){
        List<Stock> stockList = stockJpaRepository.findAllByStockNameContaining(findByNameRequestDto.getName());
        return stockList;
    }

    public Stock findByItemCode(FindByItemCodeRequestDto findByItemCodeRequestDto){
        Stock stock = stockJpaRepository.findByItemCode(findByItemCodeRequestDto.getItemCode());
        return stock;
    }

    public Page<Stock> getPageOrderBy(String sortBy, String sortOrder, Pageable pageable) {
        Page<Stock> pageOrderBy = stockRepository.getPageOrderBy(sortBy, sortOrder, pageable);
        return pageOrderBy;
    }
}
