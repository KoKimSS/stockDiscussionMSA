package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StockRepository extends StockRepositoryCustom, JpaRepository<Stock,Long>, QuerydslPredicateExecutor<Stock> {
}
