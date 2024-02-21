package com.example.stockmsanewsfeed.client.dto.request.activity;

import com.example.stockmsanewsfeed.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetPosterRequestDto extends RequestDto {
    private Long posterId;
    @Builder
    public GetPosterRequestDto(Long posterId) {
        this.posterId = posterId;
    }
}