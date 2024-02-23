package com.example.stockmsaactivity.client.dto.response.user;

import com.example.stockmsaactivity.client.dto.response.ApiResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class GetMyFollowersResponseDto extends ApiResponseDto {
    List<FollowerDto> followerList;

    public GetMyFollowersResponseDto(String code, String message,List<FollowerDto> followerList) {
        super(code, message);
        this.followerList = followerList;
    }
}

