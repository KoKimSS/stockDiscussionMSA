package com.example.stockmsaactivity.service.replyService;


import com.example.stockmsaactivity.client.dto.CreateNewsFeedRequestDto;
import com.example.stockmsaactivity.client.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.common.error.exception.DatabaseErrorException;
import com.example.stockmsaactivity.common.error.exception.InternalServerErrorException;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.domain.reply.Reply;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.repository.replyRepository.ReplyJpaRepository;
import com.example.stockmsaactivity.web.dto.request.reply.CreateReplyRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetRepliesByPosterIdRequestDto;
import com.example.stockmsaactivity.web.dto.request.reply.GetReplyRequestDto;
import com.example.stockmsaactivity.web.dto.response.reply.CreateReplyResponseDto;
import com.example.stockmsaactivity.web.dto.response.reply.GetRepliesByPosterIdResponseDto;
import com.example.stockmsaactivity.web.dto.response.reply.GetReplyResponseDto;
import com.example.stockmsaactivity.web.dto.response.reply.ReplyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyJpaRepository replyJpaRepository;
    private final PosterJpaRepository posterJpaRepository;
    private final NewsFeedApi newsFeedApi;

    @Override
    public ResponseEntity<? super CreateReplyResponseDto> createReply(CreateReplyRequestDto dto) {
        Long userId = dto.getUserId();
        Long posterId = dto.getPosterId();
        Poster poster = posterJpaRepository.findById(posterId)
                .orElseThrow(() -> new DatabaseErrorException("db 에러"));
        String contents = dto.getContents();

        Reply newReply = Reply.builder().userId(userId)
                .poster(poster)
                .contents(contents).build();
        replyJpaRepository.save(newReply);

        //나를 팔로우 하는 사람들의 뉴스피드 업데이트
        //뉴스피드 생성 서비스 호출 !
        CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                .activityType("활동 타입").build();

        try {
            newsFeedApi.createNewsFeed(createNewsFeedRequestDto);
        } catch (Exception e) {
            throw new InternalServerErrorException("internal server error");
        }
        return CreateReplyResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetReplyResponseDto> getReply(GetReplyRequestDto dto) {
        Reply reply = replyJpaRepository.findById(dto.getReplyId())
                .orElseThrow(() -> new DatabaseErrorException("db 에러"));
        ReplyDto replyDto = ReplyDto.builder()
                .replyId(reply.getId())
                .contents(reply.getContents())
                .userId(reply.getUserId())
                .build();

        return GetReplyResponseDto.success(replyDto);
    }

    @Override
    public ResponseEntity<? super GetRepliesByPosterIdResponseDto> getRepliesByPoster(GetRepliesByPosterIdRequestDto dto) {
        List<Reply> replyList = replyJpaRepository.findAllByPosterId(dto.getPosterId());
        List<ReplyDto> replyDtoList = replyList.stream().map(reply ->
                        ReplyDto.builder()
                                .replyId(reply.getId())
                                .contents(reply.getContents())
                                .userId(reply.getUserId())
                                .build())
                .collect(Collectors.toList());


        return GetRepliesByPosterIdResponseDto.success(replyDtoList);
    }
}
