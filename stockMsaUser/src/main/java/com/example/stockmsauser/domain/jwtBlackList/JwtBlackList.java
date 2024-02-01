package com.example.stockmsauser.domain.jwtBlackList;

import com.example.stockmsauser.domain.baseEntity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtBlackList extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Builder
    private JwtBlackList(String token) {
        this.token = token;
    }
}
