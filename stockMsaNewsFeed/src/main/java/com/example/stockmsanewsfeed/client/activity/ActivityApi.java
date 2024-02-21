package com.example.stockmsanewsfeed.client.activity;

import com.example.stockmsanewsfeed.client.dto.request.activity.GetPosterRequestDto;
import com.example.stockmsanewsfeed.client.dto.response.PosterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "activity", url = "http://localhost:8082/api/internal/activity")
public interface ActivityApi {
    @PostMapping(path = "/get-poster-by-id")
    PosterDto getPoster(GetPosterRequestDto dto);
}
