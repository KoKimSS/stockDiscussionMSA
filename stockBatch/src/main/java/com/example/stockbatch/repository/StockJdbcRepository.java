package com.example.stockbatch.repository;

import com.example.stockbatch.domain.Stock;
import com.example.stockbatch.domain.StockCandle;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StockJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public StockJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertStockCandles(List<StockCandle> stockCandles) {
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
                        ps.setDate(7, Date.valueOf(stockCandle.getDate()));
                    }

                    @Override
                    public int getBatchSize() {
                        return stockCandles.size();
                    }
                });
    }

    public List<String> getStockCodesWithOverThousand() {
        String sql = "SELECT code FROM stock_candle GROUP BY code HAVING COUNT(*) >= 1000";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public void cleanupStockData(List<String> stockCodes) {
        String sql = "DELETE FROM stock_candle WHERE code = ? AND id NOT IN (SELECT id FROM (SELECT id FROM stock_candle WHERE code = ? ORDER BY date DESC LIMIT 999) AS t)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                ps.setString(1, stockCodes.get(i));
                ps.setString(2, stockCodes.get(i));
            }
            @Override
            public int getBatchSize() {
                return stockCodes.size();
            }
        });
    }

    public void batchInsertStocks(List<Stock> stocks) {
        String sql = "INSERT INTO stock "
                + "(stock_name, item_code, category, accumulated_trading_volume, accumulated_trading_value, fluctuations_ratio) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Stock stock = stocks.get(i);
                        ps.setString(1, stock.getStockName());
                        ps.setString(2, stock.getItemCode());
                        ps.setString(3, stock.getCategory());
                        ps.setLong(4, stock.getAccumulatedTradingVolume());
                        ps.setLong(5, stock.getAccumulatedTradingValue());
                        ps.setDouble(6, stock.getFluctuationsRatio());
                    }

                    @Override
                    public int getBatchSize() {
                        return stocks.size();
                    }
                });
    }


}
