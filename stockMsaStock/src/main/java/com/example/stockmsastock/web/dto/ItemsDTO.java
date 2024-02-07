package com.example.stockmsastock.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemsDTO {
    private List<StockInfoDTO> items;

    // Getter and setter
}