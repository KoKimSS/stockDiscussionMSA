package com.example.stockmsaactivity.repository.replyRepository;

import com.example.stockmsaactivity.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ReplyJpaRepository extends JpaRepository<Reply,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Reply r SET r.likeCount = :likeCount WHERE r.id = :replyId")
    void addLikeCountFromRedis(@Param("replyId") Long replyId, @Param("likeCount") int likeCount);

    List<Reply> findAllByPosterId(Long posterId);
}
