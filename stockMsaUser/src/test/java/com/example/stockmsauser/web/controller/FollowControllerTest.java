package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.common.ResponseMessage;
import com.example.stockmsauser.common.ValidationMessage;
import com.example.stockmsauser.config.jwt.JwtUtil;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.restdocs.AbstractRestDocsTests;
import com.example.stockmsauser.service.followService.FollowService;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.StartFollowResponseDto;
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
import org.springframework.security.test.context.support.WithUserDetails;

import static com.example.stockmsauser.common.ResponseCode.*;
import static com.example.stockmsauser.config.jwt.JwtProperties.HEADER_STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FollowController.class})
class FollowControllerTest extends AbstractRestDocsTests {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FollowService followService;
    private MockedStatic<JwtUtil> jwtUtil;
    private final Long loginUserId = 1L;
    private final String token = "token";

    @BeforeEach
    void setUp() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(loginUserId);
        jwtUtil = mockStatic(JwtUtil.class);
        given(JwtUtil.getTokenFromHeader(any(String.class))).willReturn(token);
        given(JwtUtil.getUserIdFromToken(any(String.class))).willReturn(loginUserId);
    }

    @AfterEach
    void afterEach() {
        jwtUtil.close();
    }

    @DisplayName("팔로우를 수행.")
    @WithUserDetails
    @Test
    public void startFollow() throws Exception {
        // Given
        StartFollowRequestDto requestDto = StartFollowRequestDto.builder()
                .followerId(loginUserId)
                .followingId(123L)
                .build();

        BDDMockito.doReturn(StartFollowResponseDto.success())
                .when(followService)
                .follow(any(StartFollowRequestDto.class));

        mockMvc.perform(
                        post("/api/user/start-follow")
                                .header(HEADER_STRING, token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andDo(document("start-follow",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("followerId").type(JsonFieldType.NUMBER)
                                        .description("팔로워 아이디"),
                                fieldWithPath("followingId").type(JsonFieldType.NUMBER)
                                        .description("팔로잉 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS)
                        )
                ));
    }

    @DisplayName("팔로우를 수행시 로그인 아이디와 팔로워 아이디의 값이 같아야 합니다.")
    @WithUserDetails
    @Test
    public void startFollowWithMisMatchFollowerIdAndLoginUserId() throws Exception {
        // Given
        StartFollowRequestDto requestDto = StartFollowRequestDto.builder()
                .followerId(loginUserId + 1L)
                .followingId(123L)
                .build();

        mockMvc.perform(
                        post("/api/user/start-follow")
                                .header(HEADER_STRING, token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(CERTIFICATION_FAIL))
                .andExpect(jsonPath("$.message").value(ResponseMessage.CERTIFICATION_FAIL));
    }

    @DisplayName("팔로우를 수행 시 팔로잉 아이디는 필수 값 입니다.")
    @Test
    public void startFollowWithNullFollowing() throws Exception {
        //given
        StartFollowRequestDto requestDto = StartFollowRequestDto.builder()
                .followerId(loginUserId)
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/start-follow")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_NULL_FOLLOWING));
    }

    @DisplayName("팔로우를 수행 시 팔로워 아이디는 필수 값 입니다.")
    @Test
    public void startFollowWithNullFollower() throws Exception {
        //given
        StartFollowRequestDto requestDto = StartFollowRequestDto.builder()
                .followingId(2L)
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/start-follow")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_NULL_FOLLOWER));
    }
}