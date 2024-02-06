package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.common.ResponseCode;
import com.example.stockmsauser.common.ResponseMessage;
import com.example.stockmsauser.config.jwt.JwtUtil;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.restdocs.AbstractRestDocsTests;
import com.example.stockmsauser.service.userService.UserService;
import com.example.stockmsauser.web.dto.request.user.UpdatePasswordRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdateProfileRequestDto;
import com.example.stockmsauser.web.dto.response.user.UpdatePasswordResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdateProfileResponseDto;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.stockmsauser.common.ResponseMessage.SUCCESS;
import static com.example.stockmsauser.config.jwt.JwtProperties.HEADER_STRING;
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

@WebMvcTest(controllers = {UserController.class})
public class UserControllerTest extends AbstractRestDocsTests {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
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

    @DisplayName("패스워드 업데이트")
    @Test
    public void updatePassword() throws Exception {
        //given
        UpdatePasswordRequestDto requestDto = UpdatePasswordRequestDto.builder()
                .userId(loginUserId)
                .password("oldPass123")
                .newPassword("newPass123")
                .build();

        BDDMockito.doReturn(UpdatePasswordResponseDto.success())
                .when(userService)
                .updatePassword(any(UpdatePasswordRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/user/update-password")
                                .header(HEADER_STRING, token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andDo(document("update-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("기존 패스워드"),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING)
                                        .description("새로운 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS)
                        )
                ));
    }

    @DisplayName("프로필 업데이트")
    @Test
    public void updateProfile() throws Exception {
        //given
        UpdateProfileRequestDto requestDto = UpdateProfileRequestDto.builder()
                .userId(loginUserId)
                .name("userName")
                .imgPath("imgPath")
                .introduction("introduction")
                .build();

        BDDMockito.doReturn(UpdateProfileResponseDto.success())
                .when(userService)
                .updateProfile(any(UpdateProfileRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/user/update-profile")
                                .header(HEADER_STRING, token)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andDo(document("update-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("imgPath").type(JsonFieldType.STRING)
                                        .description("이미지 주소"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING)
                                        .description("자기소개")
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
