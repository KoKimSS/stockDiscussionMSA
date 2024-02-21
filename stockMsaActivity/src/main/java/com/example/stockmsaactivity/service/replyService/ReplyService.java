package com.example.stockmsaactivity.service.replyService;


import com.example.stockmsaactivity.web.dto.request.reply.CreateReplyRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetRepliesByPosterIdRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetReplyRequestDto;
import com.example.stockmsaactivity.web.dto.response.reply.ReplyDto;

import java.util.List;

public interface ReplyService {
    Long createReply(CreateReplyRequestDto dto);
    ReplyDto getReply(GetReplyRequestDto dto);
    List<ReplyDto> getRepliesByPoster(GetRepliesByPosterIdRequestDto dto);
}
