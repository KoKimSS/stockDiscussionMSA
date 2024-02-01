package com.example.stockmsaactivity.web.dto.request.poster;


import com.example.stockmsaactivity.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetPosterRequestDto extends RequestDto {
    private Long posterId;
    @Builder
    public GetPosterRequestDto(Long posterId) {
        this.posterId = posterId;
    }
}
