package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockJpaRepository extends JpaRepository<Stock,Long> {
    List<Stock> findAllByStockNameContaining(String name);

    Stock findByItemCode(String itemCode);
}
