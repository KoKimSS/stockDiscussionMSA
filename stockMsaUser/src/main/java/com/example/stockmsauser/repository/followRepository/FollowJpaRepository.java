package com.example.stockmsauser.repository.followRepository;

import com.example.stockmsauser.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowJpaRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFollowingId(Long followingId);
    List<Follow> findByFollowerId(Long followerId);
}
