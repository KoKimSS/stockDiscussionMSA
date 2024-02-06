package com.example.stockmsanewsfeed.web.api.activity;

import com.example.stockmsanewsfeed.web.api.dto.request.activity.GetPosterRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.response.PosterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "activity", url = "http://localhost:8082/api/internal/activity")
public interface ActivityApi {
    @PostMapping(path = "/get-poster-by-id")
    PosterDto getPoster(GetPosterRequestDto dto);
}
