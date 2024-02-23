package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.Stock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;


    @AfterEach
    void afterEach() {
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("Stock 이름을 포함하는 Stock 찾기 ")
    @Test
    public void findAllByStockNameContaining() throws Exception {
        //given
        String stockName1 = "stock1";
        String stockName2 = "stock2";

        Stock stock1 = Stock.builder()
                .stockName(stockName1).build();
        Stock stock2 = Stock.builder()
                .stockName(stockName2).build();
        stockRepository.saveAll(List.of(stock1, stock2));

        //when
        List<Stock> stockListByNameStock = stockRepository.findAllByStockNameContaining("stock");
        List<Stock> stockListByNameStock1 = stockRepository.findAllByStockNameContaining("stock1");

        //then
        Assertions.assertThat(stockListByNameStock)
                .extracting("stockName")
                .containsExactly("stock1", "stock2");

        Assertions.assertThat(stockListByNameStock1)
                .extracting("stockName")
                .containsExactly("stock1");
    }

    @DisplayName("아이템코드로 stock 찾기 ")
    @Test
    public void findByItemCode() throws Exception {
        //given
        String stockCode1 = "123456";
        String stockCode2 = "7891011";

        Stock stock1 = Stock.builder()
                .itemCode(stockCode1).build();
        Stock stock2 = Stock.builder()
                .stockName(stockCode2).build();
        stockRepository.saveAll(List.of(stock1, stock2));

        //when
        Stock byItemCode = stockRepository.findByItemCode(stockCode1);

        //then
        Assertions.assertThat(byItemCode.getItemCode()).isEqualTo(stockCode1);
    }

    @DisplayName("조건에 맞게 페이지 가져오기 - 조건 : 이름, 오름차순")
    @Test
    public void getPageOrderByName() throws Exception {
        //given
        Stock stock1 = Stock.builder().stockName("1").build();
        Stock stock2 = Stock.builder().stockName("2").build();
        Stock stock3 = Stock.builder().stockName("3").build();
        Stock stock4 = Stock.builder().stockName("4").build();
        stockRepository.saveAll(List.of(stock1, stock2, stock3, stock4));
        StockSortType name = StockSortType.NAME;
        StockSortOrder asc = StockSortOrder.ASC;
        Pageable pageable = PageRequest.of(0, 3);

        //when
        Page<Stock> pageOrderBy = stockRepository.getPageOrderBy(name, asc, pageable);

        //then
        Assertions.assertThat(pageOrderBy.getContent())
                .extracting("stockName")
                .containsExactly("1", "2", "3");
        Assertions.assertThat(pageOrderBy.getTotalElements())
                .isEqualTo(4);
    }

    @DisplayName("조건에 맞게 페이지 가져오기 - 조건 : 코드, 오름차순")
    @Test
    public void getPageOrderByCode() throws Exception {
        //given
        Stock stock1 = Stock.builder().itemCode("1").build();
        Stock stock2 = Stock.builder().itemCode("2").build();
        Stock stock3 = Stock.builder().itemCode("3").build();
        Stock stock4 = Stock.builder().itemCode("4").build();
        stockRepository.saveAll(List.of(stock1, stock2, stock3, stock4));
        StockSortType code = StockSortType.CODE;
        StockSortOrder asc = StockSortOrder.ASC;
        Pageable pageable = PageRequest.of(0, 3);

        //when
        Page<Stock> pageOrderBy = stockRepository.getPageOrderBy(code, asc, pageable);

        //then
        Assertions.assertThat(pageOrderBy.getContent())
                .extracting("itemCode")
                .containsExactly("1", "2", "3");
        Assertions.assertThat(pageOrderBy.getTotalElements())
                .isEqualTo(4);
    }
    @DisplayName("조건에 맞게 페이지 가져오기 - 조건 : 거래량, 오름차순")
    @Test
    public void getPageOrderByVolume() throws Exception {
        //given
        Stock stock1 = Stock.builder().accumulatedTradingVolume(1L).build();
        Stock stock2 = Stock.builder().accumulatedTradingVolume(2L).build();
        Stock stock3 = Stock.builder().accumulatedTradingVolume(3L).build();
        Stock stock4 = Stock.builder().accumulatedTradingVolume(4L).build();
        stockRepository.saveAll(List.of(stock1, stock2, stock3, stock4));
        StockSortType volume = StockSortType.ACCUMULVOLUME;
        StockSortOrder asc = StockSortOrder.ASC;
        Pageable pageable = PageRequest.of(0, 3);

        //when
        Page<Stock> pageOrderBy = stockRepository.getPageOrderBy(volume, asc, pageable);

        //then
        Assertions.assertThat(pageOrderBy.getContent())
                .extracting("accumulatedTradingVolume")
                .containsExactly(1L, 2L, 3L);
        Assertions.assertThat(pageOrderBy.getTotalElements())
                .isEqualTo(4);
    }
    @DisplayName("조건에 맞게 페이지 가져오기 - 조건 : 거래대금, 오름차순")
    @Test
    public void getPageOrderByValue() throws Exception {
        //given
        Stock stock1 = Stock.builder().accumulatedTradingValue(1L).build();
        Stock stock2 = Stock.builder().accumulatedTradingValue(2L).build();
        Stock stock3 = Stock.builder().accumulatedTradingValue(3L).build();
        Stock stock4 = Stock.builder().accumulatedTradingValue(4L).build();
        stockRepository.saveAll(List.of(stock1, stock2, stock3, stock4));
        StockSortType value = StockSortType.ACCUMULVALUE;
        StockSortOrder asc = StockSortOrder.ASC;
        Pageable pageable = PageRequest.of(0, 3);

        //when
        Page<Stock> pageOrderBy = stockRepository.getPageOrderBy(value, asc, pageable);

        //then
        Assertions.assertThat(pageOrderBy.getContent())
                .extracting("accumulatedTradingValue")
                .containsExactly(1L, 2L, 3L);
        Assertions.assertThat(pageOrderBy.getTotalElements())
                .isEqualTo(4);
    }
    @DisplayName("조건에 맞게 페이지 가져오기 - 조건 : 상승률, 오름차순")
    @Test
    public void getPageOrderByRatio() throws Exception {
        //given
        Stock stock1 = Stock.builder().fluctuationsRatio(1D).build();
        Stock stock2 = Stock.builder().fluctuationsRatio(2D).build();
        Stock stock3 = Stock.builder().fluctuationsRatio(3D).build();
        Stock stock4 = Stock.builder().fluctuationsRatio(4D).build();
        stockRepository.saveAll(List.of(stock1, stock2, stock3, stock4));
        StockSortType ratio = StockSortType.FLUNCRATIO;
        StockSortOrder asc = StockSortOrder.ASC;
        Pageable pageable = PageRequest.of(0, 3);

        //when
        Page<Stock> pageOrderBy = stockRepository.getPageOrderBy(ratio, asc, pageable);

        //then
        Assertions.assertThat(pageOrderBy.getContent())
                .extracting("fluctuationsRatio")
                .containsExactly(1D, 2D, 3D);
        Assertions.assertThat(pageOrderBy.getTotalElements())
                .isEqualTo(4);
    }
}