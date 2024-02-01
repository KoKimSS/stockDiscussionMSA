package com.example.stockmsauser.domain.user;

import com.example.stockmsauser.domain.baseEntity.BaseTimeEntity;
import com.example.stockmsauser.domain.follow.Follow;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
@Table(name = "\"user\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id","email","password","name","introduction","imgPath","roles"})
@Getter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    private String name;
    private String introduction;
    private String imgPath;
    private String roles; //USER, ADMIN
    @OneToMany(mappedBy = "following")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> following = new ArrayList<>();

    @Builder
    private User(Long id,String email, String password, String name, String introduction, String imgPath) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.introduction = introduction;
        this.imgPath = imgPath;
        this.roles = "USER";
    }
    public List<String> getRoleList() {
        if (!this.roles.equals(null)) {
            return Arrays.asList(this.roles.split(" "));
        }
        return new ArrayList<>();
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateProfile(String name ,String imgPath, String introduction){
        this.name=name;
        this.imgPath =imgPath;
        this.introduction=introduction;
    }
}
