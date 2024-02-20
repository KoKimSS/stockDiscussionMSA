package com.example.stockbatch.repository;

import com.example.stockbatch.domain.StockCandle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCandleJpaRepository extends JpaRepository<StockCandle,Long> {
}
