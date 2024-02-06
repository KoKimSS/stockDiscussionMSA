package com.example.stockmsanewsfeed.web.api.dto.response.user;

import com.example.stockmsanewsfeed.web.api.dto.response.ApiResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetMyFollowersResponseDto extends ApiResponseDto {
    List<FollowerDto> followerList;

    @Builder
    private GetMyFollowersResponseDto(List<FollowerDto> followerList) {
        this.followerList = followerList;
    }
}
