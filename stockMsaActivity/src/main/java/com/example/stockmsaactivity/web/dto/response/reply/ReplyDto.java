package com.example.stockmsaactivity.web.dto.response.reply;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyDto {
    private Long replyId;
    private String contents;
    private Long userId;

    @Builder
    private ReplyDto(Long replyId, String contents, Long userId) {
        this.replyId = replyId;
        this.contents = contents;
        this.userId = userId;
    }
}
