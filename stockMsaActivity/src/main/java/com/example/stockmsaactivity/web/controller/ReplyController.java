package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.common.error.exception.CertificationFailException;
import com.example.stockmsaactivity.service.replyService.ReplyService;
import com.example.stockmsaactivity.web.dto.request.reply.CreateReplyRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetRepliesByPosterIdRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetReplyRequestDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import com.example.stockmsaactivity.web.dto.response.reply.ReplyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static com.example.stockmsaactivity.common.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsaactivity.common.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsaactivity.common.jwt.JwtUtil.getUserIdFromToken;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/create-reply")
    ResponseEntity createReply(
            @Valid @RequestBody CreateReplyRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if(loginId!=userId) throw new CertificationFailException("인증 실패");

        Long replyId = replyService.createReply(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(replyId));
    }

    @PostMapping("/get-reply")
    ResponseEntity getReply(
            @Valid @RequestBody GetReplyRequestDto requestBody
    ){
        ReplyDto replyDto = replyService.getReply(requestBody);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(replyDto));
    }

    @PostMapping("/get-replies-by-poster")
    ResponseEntity getRepliesByPosterId(
            @Valid @RequestBody GetRepliesByPosterIdRequestDto requestBody
    ){
        List<ReplyDto> replyDtoList = replyService.getRepliesByPoster(requestBody);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(replyDtoList));
    }


}
