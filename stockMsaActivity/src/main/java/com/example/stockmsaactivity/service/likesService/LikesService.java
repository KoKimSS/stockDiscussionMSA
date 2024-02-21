package com.example.stockmsaactivity.service.likesService;

import com.example.stockmsaactivity.web.dto.request.likes.CreateLikesRequestDto;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;

public interface LikesService {

    Long createLikes(CreateLikesRequestDto dto);

    @Transactional
    void addReplyLikesCntToRedis(Long replyId);

    @Transactional
    void addPosterLikesCntToRedis(Long posterId);

    @Scheduled(fixedDelay = 1000L*18L)
    @Transactional
    void updateLikesCountByRedis();
}
