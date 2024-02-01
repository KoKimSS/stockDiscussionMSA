package com.example.stockmsanewsfeed.web.dto.request.newsFeed;

import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import com.example.stockmsanewsfeed.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.util.List;

import static com.example.stockmsanewsfeed.common.ValidationMessage.*;


@Data
@NoArgsConstructor
public class GetMyNewsFeedByTypesRequestDto extends RequestDto {
    @NotNull(message = NOT_NULL_USER)
    private Long userId;
    @Min(value = 0,message =PAGE_MIN_VALUE_0 )
    private int page;
    @Positive(message = PAGE_SIZE_POSITIVE )
    private int size;

    List<NewsFeedType> newsFeedTypeList;

    @Builder
    public GetMyNewsFeedByTypesRequestDto(Long userId, int page, int size, List<NewsFeedType> newsFeedTypeList) {
        this.userId = userId;
        this.page = page;
        this.size = size;
        this.newsFeedTypeList = newsFeedTypeList;
    }
}
