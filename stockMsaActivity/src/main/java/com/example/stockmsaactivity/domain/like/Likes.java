package com.example.stockmsaactivity.domain.like;

import com.example.stockmsaactivity.domain.baseEntity.BaseTimeEntity;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.domain.reply.Reply;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Likes extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "likes_id")
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;  // 예시: 글에 대한 좋아요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @Enumerated(EnumType.STRING)
    private LikeType likeType;

    @Builder
    private Likes( Long userId, Poster poster, Reply reply, LikeType likeType) {
        this.userId = userId;
        this.poster = poster;
        this.reply = reply;
        this.likeType = likeType;
    }
}
