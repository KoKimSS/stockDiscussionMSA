package com.example.stockmsaactivity.web.dto.request.reply;

import com.example.stockmsaactivity.web.dto.request.RequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetRepliesByPosterIdRequestDto extends RequestDto {
    private Long posterId;
}
