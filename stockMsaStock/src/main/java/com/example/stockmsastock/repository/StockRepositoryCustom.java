package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockRepositoryCustom {
        Page<Stock> findAllOrderedBy(String sortBy, String sortOrder, Pageable pageable);
}
