package com.example.stockmsauser.domain.follow;

import com.example.stockmsauser.domain.baseEntity.BaseTimeEntity;
import com.example.stockmsauser.domain.user.User;
import lombok.*;

import javax.persistence.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
public class Follow extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "follow_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    @Builder
    private Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

}
