package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StockJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public StockJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertStocks(List<Stock> stocks) {
        String sql = "INSERT INTO stock "
                + "(stock_name, item_code, category) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Stock stock = stocks.get(i);
                ps.setString(1, stock.getStockName());
                ps.setString(2, stock.getItemCode());
                ps.setString(3, stock.getCategory());
            }

            @Override
            public int getBatchSize() {
                return stocks.size();
            }
        });
    }
}