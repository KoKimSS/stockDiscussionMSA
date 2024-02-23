package com.example.stockmsaactivity.service.likesService;


import com.example.stockmsaactivity.client.dto.CreateNewsFeedRequestDto;
import com.example.stockmsaactivity.client.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.config.TestRedisConfig;
import com.example.stockmsaactivity.domain.like.LikeType;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.domain.reply.Reply;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.repository.replyRepository.ReplyJpaRepository;
import com.example.stockmsaactivity.web.dto.request.likes.CreateLikesRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import javax.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.example.stockmsaactivity.service.likesService.LikesServiceTest.getCreateLikesRequestDtoBuilder;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
@Import(TestRedisConfig.class)
public class LikesRedisServiceTest {
    @Autowired
    private LikesService likesService;
    @Autowired
    private PosterJpaRepository posterJpaRepository;
    @Autowired
    private ReplyJpaRepository replyJpaRepository;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private EntityManager em;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @MockBean
    private NewsFeedApi newsFeedApi;

    @Test
    void testCreateLikes() {
        //given
        BDDMockito.doNothing()
                .when(newsFeedApi).createNewsFeed(any(CreateNewsFeedRequestDto.class));

        Long userId = 1L;
        Poster poster = Poster.builder().title("title").build();
        posterJpaRepository.save(poster);
        Reply reply = Reply.builder().contents("난댓글").poster(poster).build();
        replyJpaRepository.save(reply);
        Long posterId = poster.getId();
        Long replyId = reply.getId();

        CreateLikesRequestDto requestDto1 = getCreateLikesRequestDtoBuilder(LikeType.POSTER, userId, null, posterId);
        CreateLikesRequestDto requestDto2 = getCreateLikesRequestDtoBuilder(LikeType.POSTER, userId, null, posterId);
        CreateLikesRequestDto requestDto3 = getCreateLikesRequestDtoBuilder(LikeType.REPLY, userId, replyId, posterId);
        System.out.println("poster.getLikeCount() = " + poster.getLikeCount());
        System.out.println("reply.getLikeCount() = " + reply.getLikeCount());
        likesService.createLikes(requestDto1);
        likesService.createLikes(requestDto2);
        likesService.createLikes(requestDto3);

        CountDownLatch latch = new CountDownLatch(1);
        Trigger trigger = new PeriodicTrigger(18000L); // 18 seconds
        taskScheduler.schedule(() -> {
            // Call your scheduled method here
            likesService.updateLikesCountByRedis();
            latch.countDown();
        }, trigger);

        // Wait for the scheduled task to complete
        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verify repository calls
        System.out.println(posterId);
        System.out.println(replyId);
        System.out.println(getPosterLikeCount(posterId));
        System.out.println(getReplyLikeCount(replyId));
    }

    public int getPosterLikeCount(Long posterId) {
        return posterJpaRepository.findById(posterId).get().getLikeCount();
    }

    public int getReplyLikeCount(Long replyId) {
        return replyJpaRepository.findById(replyId).get().getLikeCount();
    }
}
