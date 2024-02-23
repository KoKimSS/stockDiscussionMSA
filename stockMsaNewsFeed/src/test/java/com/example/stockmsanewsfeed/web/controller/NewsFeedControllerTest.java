package com.example.stockmsanewsfeed.web.controller;

import com.example.stockmsanewsfeed.common.error.ResponseCode;
import com.example.stockmsanewsfeed.common.error.ResponseMessage;
import com.example.stockmsanewsfeed.common.error.ValidationMessage;
import com.example.stockmsanewsfeed.common.jwt.JwtUtil;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import com.example.stockmsanewsfeed.restDocs.AbstractRestDocsTests;
import com.example.stockmsanewsfeed.service.newsFeedService.NewsFeedService;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.NewsFeedDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.NewsFeedPageDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static com.example.stockmsanewsfeed.common.error.ResponseCode.VALIDATION_FAIL;
import static com.example.stockmsanewsfeed.common.error.ResponseMessage.SUCCESS;
import static com.example.stockmsanewsfeed.common.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType.*;
import static com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType.FOLLOWING_REPLY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {NewsFeedController.class})
class NewsFeedControllerTest extends AbstractRestDocsTests {

    private final Long loginUserId = 1L;
    private final String token = "token";
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private NewsFeedService newsFeedService;
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

    @DisplayName("나의 뉴스피드 가져오기")
    @Test
    public void getMyNewsFeed() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .userId(loginUserId)
                .page(2)
                .size(2)
                .build();

        Page<NewsFeedDto> newsFeedDtoPage = createMockedPage();
        NewsFeedPageDto newsFeedPageDto = NewsFeedPageDto.pageToPageDto(newsFeedDtoPage);

        BDDMockito.doReturn(newsFeedPageDto)
                .when(newsFeedService)
                .getMyNewsFeeds(any(GetMyNewsFeedRequestDto.class));

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/newsFeed/get-myNewsFeed")
                                .header(HEADER_STRING, token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("get-my-newsFeed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("page").type(JsonFieldType.NUMBER)
                                        .description("페이지"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.contents[]").type(JsonFieldType.ARRAY)
                                        .description("뉴스피드 목록"),
                                fieldWithPath("data.contents[].userName").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("data.contents[].userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("data.contents[].activityUserId").type(JsonFieldType.NUMBER)
                                        .description("활동 유저 아이디"),
                                fieldWithPath("data.contents[].activityUserName").type(JsonFieldType.STRING)
                                        .description("활동 유저 이름"),
                                fieldWithPath("data.contents[].relatedUserId").type(JsonFieldType.NUMBER)
                                        .description("관련 유저 아이디"),
                                fieldWithPath("data.contents[].relatedUserName").type(JsonFieldType.STRING)
                                        .description("관련 유저 이름"),
                                fieldWithPath("data.contents[].relatedPosterId").type(JsonFieldType.NUMBER)
                                        .description("관련 포스터 아이디"),
                                fieldWithPath("data.contents[].relatedPosterName").type(JsonFieldType.STRING)
                                        .description("관련 포스터 이름"),
                                fieldWithPath("data.contents[].newsFeedType").type(JsonFieldType.STRING)
                                        .description("뉴스피드 타입"),
                                fieldWithPath("data.contents[].message").type(JsonFieldType.STRING)
                                        .description("뉴스피드 메시지"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                        .description("총 뉴스피드 수"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                        .description("총 페이지 수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 뉴스피드 수")
                        )));
    }

    @DisplayName("뉴스피드 가져올 시 로그인되어 있는 아이디와 요청 아이디가 같아야 합니다.")
    @Test
    public void getMyNewsFeedWithMisMatchUserAndPrincipalUser() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .userId(loginUserId + 1L)
                .page(2)
                .size(2)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/newsFeed/get-myNewsFeed")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.CERTIFICATION_FAIL))
                .andExpect(jsonPath("$.message").value(ResponseMessage.CERTIFICATION_FAIL));
    }

    @DisplayName("나의 뉴스피드를 가져올 땐 아이디 값은 필수 입니다.")
    @Test
    public void getMyNewsFeedWithNullUser() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .page(0)
                .size(2)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/newsFeed/get-myNewsFeed")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_NULL_USER));
    }

    @DisplayName("나의 뉴스피드를 가져올 때 페이지 수는 0이상입니다.")
    @Test
    public void getMyNewsFeedWithNegativePage() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .userId(loginUserId)
                .page(-1)
                .size(2)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/newsFeed/get-myNewsFeed")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.PAGE_MIN_VALUE_0));
    }

    @DisplayName("나의 뉴스피드를 가져올 때 사이즈는 양수입니다.")
    @Test
    public void getMyNewsFeedWithNotPositive() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .userId(loginUserId)
                .page(0)
                .size(0)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/newsFeed/get-myNewsFeed")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.PAGE_SIZE_POSITIVE));
    }

    @DisplayName("뉴스피드 타입을 통해 나의 뉴스피드 가져오기")
    @Test
    public void getMyNewsFeedByTypes() throws Exception {
        //given
        GetMyNewsFeedByTypesRequestDto requestDto = GetMyNewsFeedByTypesRequestDto.builder()
                .newsFeedTypeList(List.of(FOLLOWING_POST,FOLLOWING_REPLY))
                .userId(loginUserId)
                .page(2)
                .size(2)
                .build();

        Page<NewsFeedDto> newsFeedDtoPage = createMockedPage();
        NewsFeedPageDto newsFeedPageDto = NewsFeedPageDto.pageToPageDto(newsFeedDtoPage);

        BDDMockito.doReturn(newsFeedPageDto)
                .when(newsFeedService)
                .getMyNewsFeedsByType(any(GetMyNewsFeedByTypesRequestDto.class));

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/newsFeed/get-myNewsFeed-by-types")
                                .header(HEADER_STRING, token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("get-my-newsFeed-by-Types",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("page").type(JsonFieldType.NUMBER)
                                        .description("페이지"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("사이즈"),
                                fieldWithPath("newsFeedTypeList").type(JsonFieldType.ARRAY)
                                        .description("뉴스피드 타입 리스트")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.contents[]").type(JsonFieldType.ARRAY)
                                        .description("뉴스피드 목록"),
                                fieldWithPath("data.contents[].userName").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("data.contents[].userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("data.contents[].activityUserId").type(JsonFieldType.NUMBER)
                                        .description("활동 유저 아이디"),
                                fieldWithPath("data.contents[].activityUserName").type(JsonFieldType.STRING)
                                        .description("활동 유저 이름"),
                                fieldWithPath("data.contents[].relatedUserId").type(JsonFieldType.NUMBER)
                                        .description("관련 유저 아이디"),
                                fieldWithPath("data.contents[].relatedUserName").type(JsonFieldType.STRING)
                                        .description("관련 유저 이름"),
                                fieldWithPath("data.contents[].relatedPosterId").type(JsonFieldType.NUMBER)
                                        .description("관련 포스터 아이디"),
                                fieldWithPath("data.contents[].relatedPosterName").type(JsonFieldType.STRING)
                                        .description("관련 포스터 이름"),
                                fieldWithPath("data.contents[].newsFeedType").type(JsonFieldType.STRING)
                                        .description("뉴스피드 타입"),
                                fieldWithPath("data.contents[].message").type(JsonFieldType.STRING)
                                        .description("뉴스피드 메시지"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                        .description("총 뉴스피드 수"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                        .description("총 페이지 수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 뉴스피드 수")
                        )));
    }


    private Page<NewsFeedDto> createMockedPage() {
        List<NewsFeedDto> mockedDtoList = createMockedDtoList();
        return new PageImpl<>(mockedDtoList);
    }

    public static List<NewsFeedDto> createMockedDtoList() {
        NewsFeedDto dto1 = NewsFeedDto.builder()
                .userId(1L)
                .userName("User")
                .newsFeedType(FOLLOWING_POST)
                .activityUserId(2L)
                .activityUserName("ActivityUser")
                .relatedUserId(3L)
                .relatedUserName("RelatedUser")
                .relatedPosterId(1L)
                .relatedPosterName("Poster")
                .build();

        NewsFeedDto dto2 = NewsFeedDto.builder()
                .userId(2L)
                .userName("User2")
                .newsFeedType(FOLLOWING_REPLY)
                .activityUserId(3L)
                .activityUserName("ActivityUser2")
                .relatedUserId(1L)
                .relatedUserName("RelatedUser2")
                .relatedPosterId(4L)
                .relatedPosterName("Poster2")
                .build();

        return Arrays.asList(dto1, dto2);
    }
}