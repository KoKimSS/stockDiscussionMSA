package com.example.stockmsauser.service.followService;


import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.FollowerDto;

import java.util.List;

public interface FollowService {
    boolean follow(StartFollowRequestDto dto);
    List<FollowerDto> getMyFollower(GetMyFollowersRequestDto dto);
}
