package com.example.stockmsanewsfeed.web.dto.response.newsFeed;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class NewsFeedPageDto {
    private List<NewsFeedDto> contents;
    private long totalElements;
    private int totalPages;
    private int size;
    private int numberOfElements;

    public static NewsFeedPageDto pageToPageDto(Page<NewsFeedDto> newsFeedDtoPage) {
        return NewsFeedPageDto.builder()
                .contents(newsFeedDtoPage.getContent())
                .numberOfElements(newsFeedDtoPage.getNumberOfElements())
                .totalPages(newsFeedDtoPage.getTotalPages())
                .totalElements(newsFeedDtoPage.getTotalElements())
                .size(newsFeedDtoPage.getSize()).build();
    }

    @Builder
    private NewsFeedPageDto(List<NewsFeedDto> contents, long totalElements, int totalPages, int size, int numberOfElements) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.numberOfElements = numberOfElements;
    }
}
