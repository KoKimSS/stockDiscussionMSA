package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.common.error.ValidationMessage;
import com.example.stockmsaactivity.common.jwt.JwtUtil;
import com.example.stockmsaactivity.domain.like.LikeType;
import com.example.stockmsaactivity.restdocs.AbstractRestDocsTests;
import com.example.stockmsaactivity.service.likesService.LikesService;
import com.example.stockmsaactivity.web.dto.request.likes.CreateLikesRequestDto;
import com.example.stockmsaactivity.web.dto.response.likes.CreateLikesResponseDto;
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

import static com.example.stockmsaactivity.common.error.ResponseMessage.SUCCESS;
import static com.example.stockmsaactivity.common.jwt.JwtProperties.HEADER_STRING;
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

@WebMvcTest(controllers = {LikesController.class})
class LikesControllerTest extends AbstractRestDocsTests {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LikesService likesService;

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

    @DisplayName("좋아요 생성")
    @Test
    public void createLikes() throws Exception {
        //given
        CreateLikesRequestDto requestDto = CreateLikesRequestDto.builder()
                .likeType(LikeType.POSTER)
                .posterId(1234L)
                .userId(loginUserId)
                .replyId(1234L)
                .build();

        BDDMockito.doReturn(CreateLikesResponseDto.builder().likeId(1L).build())
                .when(likesService)
                .createLikes(any(CreateLikesRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/activity/create-likes")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.likeId").value(1))
                .andDo(document("create-likes",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("posterId").type(JsonFieldType.NUMBER)
                                        .description("포스터 아이디"),
                                fieldWithPath("likeType").type(JsonFieldType.STRING)
                                        .description("라이크 타입"),
                                fieldWithPath("replyId").type(JsonFieldType.NUMBER)
                                        .optional()
                                        .description("댓글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS)
                        )
                ));
    }

    @DisplayName("좋아요 생성시 로그인되어 있는 아이디와 요청 아이디가 같아야 합니다.")
    @Test
    public void createLikesWithMisMatchUserAndPrincipalUser() throws Exception {
        //given
        CreateLikesRequestDto requestDto = CreateLikesRequestDto.builder()
                .likeType(LikeType.POSTER)
                .posterId(1234L)
                .userId(loginUserId +1)
                .build();

        // when
        mockMvc.perform(
                        post("/api/activity/create-likes")
                                .header(HEADER_STRING, token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.CERTIFICATION_FAIL))
                .andExpect(jsonPath("$.message").value(ResponseMessage.CERTIFICATION_FAIL));
    }

    @DisplayName("좋아요 생성시 포스터 아이디는 필수 값 입니다.")
    @Test
    public void createLikesWithNullPoster() throws Exception {
        //given
        CreateLikesRequestDto requestDto = CreateLikesRequestDto.builder()
                .likeType(LikeType.POSTER)
                .userId(loginUserId)
                .build();

        // when
        mockMvc.perform(
                        post("/api/activity/create-likes")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_NULL_POSTER));
    }

    @DisplayName("좋아요 생성시 유저 아이디는 필수 값 입니다.")
    @Test
    public void createLikesWithNullUser() throws Exception {
        //given
        CreateLikesRequestDto requestDto = CreateLikesRequestDto.builder()
                .likeType(LikeType.POSTER)
                .posterId(loginUserId)
                .build();

        System.out.println("request : "+objectMapper.writeValueAsString(requestDto));
        // when
        mockMvc.perform(
                        post("/api/activity/create-likes")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_NULL_USER));
    }

    @DisplayName("좋아요 타입이 맞지 않습니다.")
    @Test
    public void createLikesWithNullLikeType() throws Exception {
        String invalidType = "invalidType";
        //given
        String requestBody = "{" +
                "\"userId\":"+ loginUserId +"," +
                " \"posterId\":123," +
                " \"likeType\": \""+"invalidType"+"\"," +
                "\"replyId\":123" +
                "}";

        // when
        mockMvc.perform(
                        post("/api/activity/create-likes")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_LIKE));
    }
}