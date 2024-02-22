package com.example.stockmsanewsfeed.web.dto.response.newsFeed;


import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class NewsFeedDto {
    private Long userId;
    private String userName;
    private Long activityUserId;
    private String activityUserName;
    private Long relatedUserId;
    private String relatedUserName;
    private Long relatedPosterId;
    private String relatedPosterName;
    private NewsFeedType newsFeedType;
    private String message;

    @Builder
    private NewsFeedDto(Long userId, String userName, Long activityUserId, String activityUserName, Long relatedUserId, String relatedUserName, Long relatedPosterId, String relatedPosterName, NewsFeedType newsFeedType) {
        this.userId = userId;
        this.userName = userName;
        this.activityUserId = activityUserId;
        this.activityUserName = activityUserName;
        this.relatedUserId = relatedUserId;
        this.relatedUserName = relatedUserName;
        this.relatedPosterId = relatedPosterId;
        this.relatedPosterName = relatedPosterName;
        this.newsFeedType = newsFeedType;
        this.message = MessageMapper();
    }
    private String MessageMapper() {
        if(newsFeedType== NewsFeedType.FOLLOWING_FOLLOW){
            return activityUserName + "님이 "+relatedUserName+"를 팔로우 합니다";
        }
        if(newsFeedType== NewsFeedType.FOLLOWING_REPLY){
            return activityUserName + "님이 "+relatedUserName+" 님의 " + relatedPosterName + " 글에 답글을 달았습니다.";
        }
        if(newsFeedType== NewsFeedType.FOLLOWING_LIKE){
            return activityUserName + "님이 "+relatedUserName+" 님의 " + relatedPosterName + " 글을 좋아합니다.";
        }
        if(newsFeedType== NewsFeedType.FOLLOWING_POST){
            return activityUserName + "님이 글 " + relatedPosterName + "을 작성하셨습니다.";
        }
        if(newsFeedType== NewsFeedType.MY_FOLLOW){
            return activityUserName + "님이 나를 팔로우 합니다";
        }
        if(newsFeedType== NewsFeedType.MY_REPLY){
            return activityUserName + "님이 나의 " + relatedPosterName + " 글에 답글을 달았습니다.";
        }
        if(newsFeedType== NewsFeedType.MY_LIKE){
            return activityUserName + "님이 나의 " + relatedPosterName + " 글에 좋아요를 눌렀습니다.";
        }
        throw new IllegalArgumentException("NewsFeedType 이 일치하지 않습니다");
    }
}
