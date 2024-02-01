package com.example.stockmsanewsfeed.domain.newsFeed;

import com.example.stockmsanewsfeed.domain.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
@Table
public class NewsFeed extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "news_feed_id")
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private NewsFeedType newsFeedType;  // 활동 타입
    private Long activityUserId;
    private Long relatedUserId;
    private Long relatedPosterId;  // 해당 활동이 포함된 글

    @Builder
    private NewsFeed(Long userId, NewsFeedType newsFeedType, Long activityUserId, Long relatedUserId, Long relatedPosterId) {
        this.userId = userId;
        this.newsFeedType = newsFeedType;
        this.activityUserId = activityUserId;
        this.relatedUserId = relatedUserId;
        this.relatedPosterId = relatedPosterId;
    }
}
