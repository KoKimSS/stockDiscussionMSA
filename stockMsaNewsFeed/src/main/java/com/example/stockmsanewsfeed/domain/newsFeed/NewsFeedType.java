package com.example.stockmsanewsfeed.domain.newsFeed;

public enum NewsFeedType {
    MY_REPLY("내 글에 리플을 담"),
    MY_LIKE("내 글에 좋아요를 누름"),
    MY_FOLLOW("나를 팔로우 함"),
    FOLLOWING_REPLY("팔로잉이 어떤 글에 리플을 담"),
    FOLLOWING_LIKE("팔로잉이 어떤 글에 좋아요를 함"),
    FOLLOWING_FOLLOW("팔로잉이 누군가를 팔로우 함"),
    FOLLOWING_POST("팔로잉이 글을 적음");

    private final String displayName;

    NewsFeedType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
