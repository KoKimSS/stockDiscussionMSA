package com.example.stockmsaactivity.web.dto.response.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PosterDto {

    private Long posterId;
    private String title;
    private String contents;
    private Long ownerId;
    private int likeCount;

    @Builder
    private PosterDto(Long posterId, String title, String contents, Long ownerId, int likeCount) {
        this.posterId = posterId;
        this.title = title;
        this.contents = contents;
        this.ownerId = ownerId;
        this.likeCount = likeCount;
    }
}
