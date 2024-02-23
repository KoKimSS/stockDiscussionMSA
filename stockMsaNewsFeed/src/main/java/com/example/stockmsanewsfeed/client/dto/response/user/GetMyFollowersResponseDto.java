package com.example.stockmsanewsfeed.client.dto.response.user;

import com.example.stockmsanewsfeed.client.dto.response.ApiResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetMyFollowersResponseDto extends ApiResponseDto {
    List<FollowerDto> followerList;

    @Builder
    private GetMyFollowersResponseDto(String code, String message, List<FollowerDto> followerList) {
        super(code, message);
        this.followerList = followerList;
    }
}
