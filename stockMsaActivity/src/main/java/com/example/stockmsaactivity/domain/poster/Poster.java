package com.example.stockmsaactivity.domain.poster;

import com.example.stockmsaactivity.domain.baseEntity.BaseTimeEntity;
import com.example.stockmsaactivity.domain.like.Likes;
import com.example.stockmsaactivity.domain.reply.Reply;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(of = {"id","title"})
public class Poster extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "poster_id")
    private Long id;
    private String title;
    private String contents;
    private Long userId;
    private String stockCode;
    private int likeCount;

    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();
    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public Poster(String title, String contents, Long userId) {
        this.title = title;
        this.contents = contents;
        this.userId = userId;
        this.likeCount=0;
    }

    public void incrementLikeCount(){
        likeCount++;
    }
}
