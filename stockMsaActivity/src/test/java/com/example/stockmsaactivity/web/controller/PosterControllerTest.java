package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.common.error.ValidationMessage;
import com.example.stockmsaactivity.common.jwt.JwtUtil;
import com.example.stockmsaactivity.restdocs.AbstractRestDocsTests;
import com.example.stockmsaactivity.service.posterService.PosterService;
import com.example.stockmsaactivity.web.dto.request.poster.CreatePosterRequestDto;
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

import static com.example.stockmsaactivity.common.error.ResponseCode.VALIDATION_FAIL;
import static com.example.stockmsaactivity.common.jwt.JwtProperties.HEADER_STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PosterController.class})
class PosterControllerTest extends AbstractRestDocsTests {

    private final Long loginUserId = 1L;
    private final String token = "token";
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PosterService posterService;
    private MockedStatic<JwtUtil> jwtUtil;

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

    @DisplayName("포스터 생성")
    @Test
    public void createPoster() throws Exception {
        //given
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.builder()
                .userId(loginUserId)
                .title("제목")
                .contents("컨텐츠")
                .build();

        Long posterId = 1L;
        BDDMockito.doReturn(posterId)
                .when(posterService)
                .createPoster(any(CreatePosterRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/activity/create-poster")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .header(HEADER_STRING, token)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.data").value(posterId))
                .andDo(document("create-poster",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("contents").type(JsonFieldType.STRING)
                                        .description("컨텐츠")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("포스터 아이디")
                        )
                ));
    }

    @DisplayName("포스터 생성시 시 로그인되어 있는 아이디와 요청 아이디가 같아야 합니다.")
    @Test
    public void createLikesWithMisMatchUserAndLoginUser() throws Exception {
        //given
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.builder()
                .userId(loginUserId + 1L)
                .title("제목")
                .contents("컨텐츠")
                .build();
        // when
        mockMvc.perform(
                        post("/api/activity/create-poster")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .header(HEADER_STRING, token)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.CERTIFICATION_FAIL))
                .andExpect(jsonPath("$.message").value(ResponseMessage.CERTIFICATION_FAIL));
    }

    @DisplayName("포스터 생성시 시 포스터 제목은 필수 값 입니다.")
    @Test
    public void createLikesWithBlankTitle() throws Exception {
        //given
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.builder()
                .userId(loginUserId)
                .title("")
                .contents("컨텐츠")
                .build();
        // when
        mockMvc.perform(
                        post("/api/activity/create-poster")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .header(HEADER_STRING, token)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_BLANK_TITLE));
    }

    @DisplayName("포스터 생성시 시 포스터 내용은 필수 값 입니다.")
    @Test
    public void createLikesWithBlankContents() throws Exception {
        //given
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.builder()
                .userId(loginUserId)
                .title("제목")
                .contents("")
                .build();
        // when
        mockMvc.perform(
                        post("/api/activity/create-poster")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .header(HEADER_STRING, token)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_BLANK_CONTENTS));
    }
}
