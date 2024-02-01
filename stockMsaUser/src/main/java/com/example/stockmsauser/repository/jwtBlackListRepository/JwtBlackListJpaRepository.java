package com.example.stockmsauser.repository.jwtBlackListRepository;


import com.example.stockmsauser.domain.jwtBlackList.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlackListJpaRepository extends JpaRepository<JwtBlackList,Long> {
    boolean existsByToken(String token);
}
