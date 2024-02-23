package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.common.error.ValidationMessage;
import com.example.stockmsaactivity.common.jwt.JwtUtil;
import com.example.stockmsaactivity.restdocs.AbstractRestDocsTests;
import com.example.stockmsaactivity.service.posterService.PosterService;
import com.example.stockmsaactivity.web.dto.request.poster.CreatePosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPostersByStockCodeRequest;
import com.example.stockmsaactivity.web.dto.response.poster.PosterDto;
import com.example.stockmsaactivity.web.dto.response.poster.PosterPageDto;
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

import java.util.List;

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

    @DisplayName("주식 코드로 포스터 검색")
    @Test
    public void getPosterByStockCode() throws Exception {
        //given
        GetPostersByStockCodeRequest requestDto = GetPostersByStockCodeRequest.builder()
                .page(0)
                .size(2)
                .stockCode("123456")
                .build();

        PosterDto posterDto1 = PosterDto.builder()
                .ownerId(1L)
                .likeCount(10)
                .posterId(1L)
                .contents("콘텐츠1")
                .title("타이틀1")
                .stockCode("123456")
                .build();
        PosterDto posterDto2 = PosterDto.builder()
                .ownerId(1L)
                .likeCount(10)
                .posterId(2L)
                .contents("콘텐츠2")
                .title("타이틀2")
                .stockCode("123456")
                .build();
        PosterPageDto posterPageDto = PosterPageDto.builder()
                .contents(List.of(posterDto1, posterDto2))
                .size(2)
                .numberOfElements(2)
                .totalElements(2)
                .totalPages(1).build();
        BDDMockito.doReturn(posterPageDto)
                .when(posterService)
                .getPosterByStockCode(any(GetPostersByStockCodeRequest.class));

        // when
        mockMvc.perform(
                        post("/api/activity/get-posters-by-stockCode")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("get-posters-by-stockCode",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("page").type(JsonFieldType.NUMBER)
                                        .description("페이지"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("사이즈"),
                                fieldWithPath("stockCode").type(JsonFieldType.STRING)
                                        .description("주식 코드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                        .description("전체 갯수"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                        .description("전체 페이지 수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("사이즈"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("데이터 수"),
                                fieldWithPath("data.contents").type(JsonFieldType.ARRAY)
                                        .description("포스터 리스트"),
                                fieldWithPath("data.contents[].posterId").type(JsonFieldType.NUMBER)
                                        .description("포스터 아이디"),
                                fieldWithPath("data.contents[].title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("data.contents[].contents").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("data.contents[].ownerId").type(JsonFieldType.NUMBER)
                                        .description("소유자 아이디"),
                                fieldWithPath("data.contents[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 수"),
                                fieldWithPath("data.contents[].stockCode").type(JsonFieldType.STRING)
                                        .description("주식 코드")
                        )
                ));
    }

    @DisplayName("id로 포스터 검색")
    @Test
    public void getPosterById() throws Exception {
        //given
        GetPosterRequestDto requestDto = GetPosterRequestDto.builder()
                .posterId(1L)
                .build();

        Long posterId = 1L;
        PosterDto posterDto = PosterDto.builder()
                .ownerId(1L)
                .likeCount(10)
                .posterId(posterId)
                .contents("콘텐츠")
                .title("타이틀")
                .stockCode("123456")
                .build();
        BDDMockito.doReturn(posterDto)
                .when(posterService)
                .getPoster(any(GetPosterRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/activity/get-poster-by-id")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("get-poster-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("posterId").type(JsonFieldType.NUMBER)
                                        .description("포스터 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.ownerId").type(JsonFieldType.NUMBER)
                                        .description("소유자 아이디"),
                                fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 수"),
                                fieldWithPath("data.posterId").type(JsonFieldType.NUMBER)
                                        .description("포스터 아이디"),
                                fieldWithPath("data.contents").type(JsonFieldType.STRING)
                                        .description("포스터 내용"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("포스터 제목"),
                                fieldWithPath("data.stockCode").type(JsonFieldType.STRING)
                                        .description("주식 코드")
                        )
                ));
    }
}
