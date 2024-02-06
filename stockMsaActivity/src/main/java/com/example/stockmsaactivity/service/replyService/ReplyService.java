package com.example.stockmsaactivity.service.replyService;


import com.example.stockmsaactivity.web.dto.request.reply.CreateReplyRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetRepliesByPosterIdRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetReplyRequestDto;
import com.example.stockmsaactivity.web.dto.response.reply.CreateReplyResponseDto;
import com.example.stockmsaactivity.web.dto.response.reply.GetRepliesByPosterIdResponseDto;
import com.example.stockmsaactivity.web.dto.response.reply.GetReplyResponseDto;
import org.springframework.http.ResponseEntity;

public interface ReplyService {
    ResponseEntity<? super CreateReplyResponseDto> createReply(CreateReplyRequestDto dto);
    ResponseEntity<? super GetReplyResponseDto> getReply(GetReplyRequestDto dto);
    ResponseEntity<? super GetRepliesByPosterIdResponseDto> getRepliesByPoster(GetRepliesByPosterIdRequestDto dto);
}
