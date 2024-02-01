package com.example.stockmsanewsfeed.web.api.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowerDto {

    private String followerName;
    private Long followerId;


    @Builder
    private FollowerDto(String followerName, Long followerId) {
        this.followerName = followerName;
        this.followerId = followerId;
    }
}
