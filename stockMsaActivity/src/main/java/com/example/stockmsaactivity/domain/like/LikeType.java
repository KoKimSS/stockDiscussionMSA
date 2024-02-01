package com.example.stockmsaactivity.domain.like;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum LikeType {
    POSTER,REPLY;

    LikeType() {
    }

    @JsonCreator
    public static LikeType parsing(String inputValue) {
        return Stream.of(LikeType.values())
                .filter(likeType -> likeType.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
