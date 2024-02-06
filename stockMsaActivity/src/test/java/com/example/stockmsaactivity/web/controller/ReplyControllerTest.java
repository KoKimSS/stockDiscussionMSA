package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.common.ResponseCode;
import com.example.stockmsaactivity.config.jwt.JwtUtil;
import com.example.stockmsaactivity.restdocs.AbstractRestDocsTests;
import com.example.stockmsaactivity.service.replyService.ReplyService;
import com.example.stockmsaactivity.web.dto.request.reply.CreateReplyRequestDto;
import com.example.stockmsaactivity.web.dto.response.reply.CreateReplyResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.example.stockmsaactivity.common.ResponseMessage.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {ReplyController.class})
public class ReplyControllerTest extends AbstractRestDocsTests {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReplyService replyService;
    private MockedStatic<JwtUtil> jwtUtil;
    private final Long loginUserId = 1L;
    private final String token = "token";
    @BeforeEach
    void setUp() {
        jwtUtil = mockStatic(JwtUtil.class);
        given(JwtUtil.isValidToken(any(String.class))).willReturn(true);
        given(JwtUtil.getTokenFromHeader(any(String.class))).willReturn(token);
        given(JwtUtil.getUserIdFromToken(any(String.class))).willReturn(loginUserId);
    }

    @AfterEach
    void afterEach() {
        jwtUtil.close();
    }

    @DisplayName("댓글 생성")
    @Test
    public void createReply() throws Exception {
        //given
        CreateReplyRequestDto requestDto = CreateReplyRequestDto.builder()
                .userId(loginUserId)
                .posterId(1L)
                .contents("contents")
                .build();

        BDDMockito.doReturn(CreateReplyResponseDto.success())
                .when(replyService)
                .createReply(any(CreateReplyRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/activity/create-reply")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andDo(document("create-reply",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("contents").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("posterId").type(JsonFieldType.NUMBER)
                                        .description("포스터 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS)
                        )
                ));
    }

}
