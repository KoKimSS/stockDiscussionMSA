package com.example.stockbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Stock2 {

    private String name;
    private String code;

    @Builder
    private Stock2(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
