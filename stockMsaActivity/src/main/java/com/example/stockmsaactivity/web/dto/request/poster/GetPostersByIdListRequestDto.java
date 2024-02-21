package com.example.stockmsaactivity.web.dto.request.poster;


import com.example.stockmsaactivity.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.example.stockmsaactivity.common.error.ValidationMessage.*;


@Data
@NoArgsConstructor
public class GetPostersByIdListRequestDto extends RequestDto {

    @NotNull(message = NOT_NULL_POSTER)
    private List<Long> posterIdList;

    @Builder
    private GetPostersByIdListRequestDto(List<Long> posterIdList) {
        this.posterIdList = posterIdList;
    }
}
