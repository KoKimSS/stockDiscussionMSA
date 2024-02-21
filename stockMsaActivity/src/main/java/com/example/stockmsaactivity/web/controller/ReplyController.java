package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.common.error.exception.CertificationFailException;
import com.example.stockmsaactivity.service.replyService.ReplyService;
import com.example.stockmsaactivity.web.dto.request.reply.CreateReplyRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetRepliesByPosterIdRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetReplyRequestDto;
import com.example.stockmsaactivity.web.dto.response.reply.CreateReplyResponseDto;
import com.example.stockmsaactivity.web.dto.response.reply.GetRepliesByPosterIdResponseDto;
import com.example.stockmsaactivity.web.dto.response.reply.GetReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.stockmsaactivity.common.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsaactivity.common.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsaactivity.common.jwt.JwtUtil.getUserIdFromToken;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/create-reply")
    ResponseEntity<?super CreateReplyResponseDto> createReply(
            @Valid @RequestBody CreateReplyRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if(loginId!=userId) throw new CertificationFailException("인증 실패");

        ResponseEntity<? super CreateReplyResponseDto> response = replyService.createReply(requestBody);
        return response;
    }

    @PostMapping("/get-reply")
    ResponseEntity<?super GetReplyResponseDto> getReply(
            @Valid @RequestBody GetReplyRequestDto requestBody
    ){
        ResponseEntity<? super GetReplyResponseDto> response = replyService.getReply(requestBody);
        return response;
    }

    @PostMapping("/get-replies-by-poster")
    ResponseEntity<?super GetRepliesByPosterIdResponseDto> getRepliesByPosterId(
            @Valid @RequestBody GetRepliesByPosterIdRequestDto requestBody
    ){
        ResponseEntity<? super GetRepliesByPosterIdResponseDto> response = replyService.getRepliesByPoster(requestBody);
        return response;
    }


}
