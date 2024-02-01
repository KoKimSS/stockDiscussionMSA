package com.example.stockmsanewsfeed.repository.newsFeedRepository;

import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeed;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsFeedJpaRepository extends JpaRepository<NewsFeed,Long> {
    // 유저 아이디와 뉴스피드 타입을 이용하여 페이징으로 뉴스피드 찾기
    Page<NewsFeed> findByUserId(Long userId,Pageable pageable);
    Page<NewsFeed> findByUserIdAndNewsFeedTypeIn(Long userId, List<NewsFeedType> newsFeedType, Pageable pageable);
    List<NewsFeed> findAllByUserId(Long userId);
}
