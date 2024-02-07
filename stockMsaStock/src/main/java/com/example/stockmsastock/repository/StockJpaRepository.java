package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<Stock,Long> {

}
