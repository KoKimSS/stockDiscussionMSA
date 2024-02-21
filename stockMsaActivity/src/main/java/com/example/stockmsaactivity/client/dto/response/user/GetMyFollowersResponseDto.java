package com.example.stockmsaactivity.client.dto.response.user;

import com.example.stockmsaactivity.client.dto.response.ApiResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class GetMyFollowersResponseDto extends ApiResponseDto {
    List<FollowerDto> followerList;
}

