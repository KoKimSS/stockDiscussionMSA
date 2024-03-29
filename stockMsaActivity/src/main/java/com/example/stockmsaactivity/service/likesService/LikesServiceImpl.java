package com.example.stockmsaactivity.service.likesService;


import com.example.stockmsaactivity.client.dto.CreateNewsFeedRequestDto;
import com.example.stockmsaactivity.client.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.common.error.exception.DatabaseErrorException;
import com.example.stockmsaactivity.common.error.exception.InternalServerErrorException;
import com.example.stockmsaactivity.common.error.exception.ValidationFailException;
import com.example.stockmsaactivity.domain.like.LikeType;
import com.example.stockmsaactivity.domain.like.Likes;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.domain.reply.Reply;
import com.example.stockmsaactivity.kafka.KafkaProducer;
import com.example.stockmsaactivity.repository.likeRepository.LikesJpaRepository;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.repository.replyRepository.ReplyJpaRepository;
import com.example.stockmsaactivity.web.dto.request.likes.CreateLikesRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Set;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {
    private final LikesJpaRepository likesJpaRepository;
    private final PosterJpaRepository posterJpaRepository;
    private final ReplyJpaRepository replyJpaRepository;
    private final RedisTemplate<String, String> redisTemplate; // Inject RedisTemplate
    private final KafkaProducer kafkaProducer;

    @Override
    @Transactional
    public Long createLikes(CreateLikesRequestDto dto) {

        LikeType likeType = dto.getLikeType();
        Long posterId = dto.getPosterId(); //@NotBlank 이다
        Long replyId = dto.getReplyId();
        Long userId = dto.getUserId();
        if (likeType == LikeType.REPLY && replyId == null) throw new ValidationFailException("validation fail");

        Poster poster = posterJpaRepository.findById(posterId)
                .orElseThrow(() -> new DatabaseErrorException("db 에러"));

        Likes.LikesBuilder likesBuilder = Likes.builder().likeType(likeType)
                .userId(userId)
                .poster(poster);

        if (likeType == LikeType.REPLY) {
            Reply reply = replyJpaRepository.findById(replyId)
                    .orElseThrow(() -> new DatabaseErrorException("db 에러"));
            likesBuilder.reply(reply);
            addReplyLikesCntToRedis(replyId);
        }
        if (likeType == LikeType.POSTER) {
            addPosterLikesCntToRedis(posterId);
            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .userId(userId)
                    .relatedPosterId(posterId)
                    .relatedUserId(poster.getUserId())
                    .activityType("LIKE")
                    .build();
            //뉴스피드 생성 서비스 호출 !
            try {
                kafkaProducer.createNewsFeed(createNewsFeedRequestDto);
            } catch (Exception e) {
                throw new InternalServerErrorException("internal server error");
            }
        }

        Likes newLikes = likesBuilder.build();
        Likes save = likesJpaRepository.save(newLikes);

        return save.getId();
    }

    @Transactional
    @Override
    public void addReplyLikesCntToRedis(Long replyId) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        String key = "replyId::" + replyId;
        String hashkey = "replyLikes";

        if (hashOperations.get(key, hashkey) == null) {
            hashOperations.put(key, hashkey, String.valueOf(replyJpaRepository.findById(replyId).get().getLikeCount()));
            hashOperations.increment(key, hashkey, 1L);
        } else {
            hashOperations.increment(key, hashkey, 1L);
        }
    }

    @Transactional
    @Override
    public void addPosterLikesCntToRedis(Long posterId) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        String key = "posterId::" + posterId;
        String hashkey = "posterLikes";
        if (hashOperations.get(key, hashkey) == null) {
            int posterLikeCount = posterJpaRepository.findById(posterId).get().getLikeCount();
            hashOperations.put(key, hashkey, String.valueOf(posterLikeCount));
            hashOperations.increment(key, hashkey, 1L);
        } else {
            hashOperations.increment(key, hashkey, 1L);
        }
    }

    @Scheduled(fixedDelay = 1000L * 18L)
    @Transactional
    @Override
    public void updateLikesCountByRedis() {
        String hashkey = "posterLikes";
        Set<String> Rediskey = redisTemplate.keys("posterId*");
        Iterator<String> it = Rediskey.iterator();
        while (it.hasNext()) {
            String keyPattern = it.next();
            Long posterId = Long.parseLong(keyPattern.split("::")[1]);
            if (redisTemplate.opsForHash().get(keyPattern, hashkey) == null) {
                break;
            }
            Integer likesCount = Integer.parseInt((String.valueOf(redisTemplate.opsForHash().get(keyPattern, hashkey))));
            posterJpaRepository.addLikeCountFromRedis(posterId, likesCount);
            redisTemplate.opsForHash().delete(keyPattern, hashkey);
        }

        hashkey = "replyLikes";
        Rediskey = redisTemplate.keys("replyId*");
        it = Rediskey.iterator();
        while (it.hasNext()) {
            String keyPattern = it.next();
            Long replyId = Long.parseLong(keyPattern.split("::")[1]);
            if (redisTemplate.opsForHash().get(keyPattern, hashkey) == null) {
                break;
            }
            int likesCount = Integer.parseInt((String.valueOf(redisTemplate.opsForHash().get(keyPattern, hashkey))));
            replyJpaRepository.addLikeCountFromRedis(replyId, likesCount);
            redisTemplate.opsForHash().delete(keyPattern, hashkey);
        }
    }
}
