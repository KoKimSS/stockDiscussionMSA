package com.example.stockmsaactivity.repository.posterRepository;

import com.example.stockmsaactivity.domain.poster.Poster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PosterJpaRepository extends JpaRepository<Poster,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Poster p SET p.likeCount = :likeCount WHERE p.id = :posterId")
    void addLikeCountFromRedis(@Param("posterId") Long posterId, @Param("likeCount") int likeCount);

    List<Poster> findAllByUserId(Long userId);

    List<Poster> findAllByIdIn(List<Long> id);
}
