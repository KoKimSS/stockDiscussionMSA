package com.example.stockmsaactivity.web.controller;

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

import javax.validation.Valid;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/create-reply")
    ResponseEntity<?super CreateReplyResponseDto> createReply(
//            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody CreateReplyRequestDto requestBody
    ){
//        Long loginId = principalDetails.getUser().getId();
//        Long userId = requestBody.getUserId();
//        if(loginId!= userId) return ApiResponseDto.certificationFail();

        ResponseEntity<? super CreateReplyResponseDto> response = replyService.createReply(requestBody);
        return response;
    }

    @PostMapping("/get-reply")
    ResponseEntity<?super GetReplyResponseDto> getReply(
//            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody GetReplyRequestDto requestBody
    ){
//        Long loginId = principalDetails.getUser().getId();
//        Long userId = requestBody.getUserId();
//        if(loginId!= userId) return ApiResponseDto.certificationFail();

        ResponseEntity<? super GetReplyResponseDto> response = replyService.getReply(requestBody);
        return response;
    }

    @PostMapping("/get-replies-by-poster")
    ResponseEntity<?super GetRepliesByPosterIdResponseDto> getRepliesByPosterId(
//            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody GetRepliesByPosterIdRequestDto requestBody
    ){
//        Long loginId = principalDetails.getUser().getId();
//        Long userId = requestBody.getUserId();
//        if(loginId!= userId) return ApiResponseDto.certificationFail();

        ResponseEntity<? super GetRepliesByPosterIdResponseDto> response = replyService.getRepliesByPoster(requestBody);
        return response;
    }


}
