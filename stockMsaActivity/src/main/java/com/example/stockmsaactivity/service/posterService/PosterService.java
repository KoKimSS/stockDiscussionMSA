package com.example.stockmsaactivity.service.posterService;

import com.example.stockmsaactivity.web.dto.request.poster.*;
import com.example.stockmsaactivity.web.dto.response.poster.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PosterService {

    Long createPoster(CreatePosterRequestDto dto);
    List<PosterDto> getMyPoster(GetMyPosterRequestDto dto);
    PosterDto getPoster(GetPosterRequestDto dto);
    List<PosterDto> getPosterByIdList(GetPostersByIdListRequestDto dto);

    Page<PosterDto> getPosterByStockCode(GetPostersByStockCodeRequest dto);
}
