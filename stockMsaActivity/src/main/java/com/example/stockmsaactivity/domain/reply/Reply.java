package com.example.stockmsaactivity.domain.reply;

import com.example.stockmsaactivity.domain.baseEntity.BaseTimeEntity;
import com.example.stockmsaactivity.domain.poster.Poster;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;
    private Long userId;
    private int likeCount;

    @Builder
    public Reply(String contents, Poster poster, Long userId) {
        this.contents = contents;
        this.poster = poster;
        this.userId = userId;
        likeCount=0;
    }
}
