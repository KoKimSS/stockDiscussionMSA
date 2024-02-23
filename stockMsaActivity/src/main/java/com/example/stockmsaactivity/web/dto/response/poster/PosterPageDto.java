package com.example.stockmsaactivity.web.dto.response.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class PosterPageDto {
    private List<PosterDto> contents;
    private long totalElements;
    private int totalPages;
    private int size;
    private int numberOfElements;

    public static PosterPageDto pageToPageDto(Page<PosterDto> newsFeedDtoPage) {
        return PosterPageDto.builder()
                .contents(newsFeedDtoPage.getContent())
                .numberOfElements(newsFeedDtoPage.getNumberOfElements())
                .totalPages(newsFeedDtoPage.getTotalPages())
                .totalElements(newsFeedDtoPage.getTotalElements())
                .size(newsFeedDtoPage.getSize()).build();
    }

    @Builder
    private PosterPageDto(List<PosterDto> contents, long totalElements, int totalPages, int size, int numberOfElements) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.numberOfElements = numberOfElements;
    }
}
