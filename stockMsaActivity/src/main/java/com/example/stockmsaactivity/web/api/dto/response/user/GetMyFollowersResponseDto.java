package com.example.stockmsaactivity.web.api.dto.response.user;

import com.example.stockmsaactivity.web.api.dto.response.ApiResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class GetMyFollowersResponseDto extends ApiResponseDto {
    List<FollowerDto> followerList;
}

