package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.StockCandle;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StockCandleJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public StockCandleJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertStocks(List<StockCandle> stockCandles) {
        String sql = "INSERT INTO stock_candle "
                + "(open, low, high, close, volume, code, date ) VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        StockCandle stockCandle = stockCandles.get(i);
                        ps.setInt(1, stockCandle.getOpen());
                        ps.setInt(2, stockCandle.getLow());
                        ps.setInt(3, stockCandle.getHigh());
                        ps.setInt(4, stockCandle.getClose());
                        ps.setInt(5, stockCandle.getVolume());
                        ps.setString(6, stockCandle.getCode());
                        ps.setString(7, stockCandle.getDate());
                    }

                    @Override
                    public int getBatchSize() {
                        return stockCandles.size();
                    }
                });
    }

}
