package com.example.stockmsaactivity.repository.likeRepository;


import com.example.stockmsaactivity.domain.like.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesJpaRepository extends JpaRepository<Likes,Long> {

}
