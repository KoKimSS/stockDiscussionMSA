package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.StockPrice;
import org.springframework.data.repository.CrudRepository;

public interface StockPriceRedisRepository extends CrudRepository<StockPrice,String> {
}
