package com.example.stockmsastock.service;

import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.repository.StockRepository;
import com.example.stockmsastock.repository.StockSortOrder;
import com.example.stockmsastock.repository.StockSortType;
import com.example.stockmsastock.web.dto.StockPageDto;
import com.example.stockmsastock.web.dto.request.FindByItemCodeRequestDto;
import com.example.stockmsastock.web.dto.request.FindByNameRequestDto;
import com.example.stockmsastock.web.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<StockDto> findByName(FindByNameRequestDto findByNameRequestDto){
        List<Stock> stockList = stockRepository.findAllByStockNameContaining(findByNameRequestDto.getName());
        return stockList.stream()
                .map(s->getStockDtoBy(s))
                .collect(Collectors.toList());
    }

    public StockDto findByItemCode(FindByItemCodeRequestDto findByItemCodeRequestDto){
        Stock stock = stockRepository.findByItemCode(findByItemCodeRequestDto.getItemCode());
        return getStockDtoBy(stock);
    }

    public StockPageDto getPageOrderBy(StockSortType sortBy, StockSortOrder sortOrder, Pageable pageable) {
        Page<Stock> stockPage = stockRepository.getPageOrderBy(sortBy, sortOrder, pageable);
        return StockPageDto.builder()
                .content(stockPage.getContent().stream()
                        .map(s->getStockDtoBy(s)).collect(Collectors.toList()))
                .numberOfElements(stockPage.getNumberOfElements())
                .totalPages(stockPage.getTotalPages())
                .totalElements(stockPage.getTotalElements())
                .size(stockPage.getSize()).build();
    }

    private static StockDto getStockDtoBy(Stock s) {
        return StockDto.builder()
                .fluctuationsRatio(s.getFluctuationsRatio())
                .itemCode(s.getItemCode())
                .stockName(s.getStockName())
                .id(s.getId())
                .accumulatedTradingVolume(s.getAccumulatedTradingVolume())
                .accumulatedTradingValue(s.getAccumulatedTradingValue())
                .category(s.getCategory()).build();
    }


}
