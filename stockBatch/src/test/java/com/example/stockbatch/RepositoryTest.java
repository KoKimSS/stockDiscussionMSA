package com.example.stockbatch;

import com.example.stockbatch.repository.StockJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
@Slf4j
public class RepositoryTest {

    @Autowired
    StockJpaRepository stockJpaRepository;

    @DisplayName("스톡 리포지토리 테스트")
    @Test
    public void testRepository () throws Exception {
        //given
        IntStream.rangeClosed(1,100)
                .forEach(num->
                        log.info(String.valueOf(num)));
        //when

        //then

    }
}
