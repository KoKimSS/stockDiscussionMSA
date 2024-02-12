package com.example.stockbatch.repository;

import com.example.stockbatch.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockJpaRepository extends JpaRepository<Stock,Long> {

    @Query("SELECT s.itemCode FROM Stock s")
    List<String> findAllItemCodes(); // 모든 Stock 엔티티의 itemCode만 가져오는 메서드
}
