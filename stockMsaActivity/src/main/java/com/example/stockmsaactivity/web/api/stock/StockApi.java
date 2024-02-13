package com.example.stockmsaactivity.web.api.stock;


import com.example.stockmsaactivity.web.api.dto.CreateNewsFeedRequestDto;
import com.example.stockmsaactivity.web.api.dto.request.stock.FindByItemCodeRequestDto;
import com.example.stockmsaactivity.web.api.dto.response.stock.StockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "stock",url = "http://localhost:8084/api/stock/")
public interface StockApi {
    @PostMapping("/find-by-itemCode")
    StockDto getStockByCode(FindByItemCodeRequestDto findByItemCodeRequestDto);
}
