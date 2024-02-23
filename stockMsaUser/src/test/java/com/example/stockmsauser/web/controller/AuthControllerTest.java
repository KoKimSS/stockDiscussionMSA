package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;
import com.example.stockmsauser.restdocs.AbstractRestDocsTests;
import com.example.stockmsauser.service.authService.AuthService;
import com.example.stockmsauser.web.dto.request.auth.*;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.example.stockmsauser.common.error.ResponseCode.VALIDATION_FAIL;
import static com.example.stockmsauser.common.error.ResponseMessage.SUCCESS;
import static com.example.stockmsauser.common.error.ValidationMessage.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest extends AbstractRestDocsTests {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private static SignUpRequestDto getBuild(String name, String password, String introduction, String imgPath, String certificationNumber, String email) {
        return SignUpRequestDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .introduction(introduction)
                .imgPath(imgPath)
                .certificationNumber(certificationNumber).build();
    }

    @DisplayName("이메일 중복을 확인한다.")
    @Test
    public void emailCheck() throws Exception {
        //given
        EmailCheckRequestDto requestDto = EmailCheckRequestDto.builder()
                .email("seungsu@gmail.com")
                .build();

        BDDMockito.doReturn(true).when(authService)
                .emailCheck(any(EmailCheckRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/user/email-check")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").value(true))
                .andDo(document("email-check",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부")
                        )
                ));
    }

    @DisplayName("이메일 중복을 확인할 때 이메일은 필수 값이다.")
    @Test
    public void emailCheckWithBlank() throws Exception {
        //given
        EmailCheckRequestDto requestDto = EmailCheckRequestDto.builder()
                .email("")
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/email-check")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_EMAIL));
    }

    @DisplayName("이메일 중복을 확인할 때 이메일형식에 맞아야 한다.")
    @Test
    public void emailCheckWithNotEmail() throws Exception {
        //given
        EmailCheckRequestDto requestDto = EmailCheckRequestDto.builder()
                .email("JustName")
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/email-check")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_EMAIL));
    }

    @DisplayName("이메일로 인증코드 발급 한다.")
    @Test
    public void emailCertification() throws Exception {
        //given
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
                .email("seungsu@gmail.com")
                .build();

        BDDMockito.doReturn(true).when(authService)
                .emailCertification(any(EmailCertificationRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/user/email-certification")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andDo(document("email-certification",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부"),
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지")
                        )
                ));
    }

    @DisplayName("이메일로 인증코드 발급 할 때 이메일은 필수 값이다.")
    @Test
    public void emailCertificationWithBlank() throws Exception {
        //given
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
                .email("")
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/email-certification")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_EMAIL));
    }

    @DisplayName("이메일로 인증코드 발급 할 때 이메일형식에 맞아야 한다.")
    @Test
    public void emailCertificationWithNotEmail() throws Exception {
        //given
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
                .email("JustName")
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/email-certification")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_EMAIL));
    }

    @DisplayName("인증코드 확인")
    @Test
    public void checkCertification() throws Exception {
        //given
        CheckCertificationUserRequestDto requestDto = CheckCertificationUserRequestDto.builder()
                .certificationNumber("1234")
                .email("seungsu@gmail.com")
                .build();

        BDDMockito.doReturn(true).when(authService)
                .checkCertification(any(CheckCertificationRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/user/check-certification")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").value(true))
                .andDo(document("check-certification",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("certificationNumber").type(JsonFieldType.STRING)
                                        .description("인증번호 4자리")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부")
                        )
                ));
    }

    @DisplayName("인증 코드 확인 할 때 이메일은 필수 값이다.")
    @Test
    public void checkCertificationWithBlankEmail() throws Exception {
        //given
        CheckCertificationUserRequestDto requestDto = CheckCertificationUserRequestDto.builder()
                .certificationNumber("1234")
                .email("")
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/check-certification")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_EMAIL));
    }

    @DisplayName("인증 코드 확인 할 때 이메일형식에 맞아야 한다.")
    @Test
    public void checkCertificationWithNotEmail() throws Exception {
        //given
        CheckCertificationUserRequestDto requestDto = CheckCertificationUserRequestDto.builder()
                .certificationNumber("1234")
                .email("JustName")
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/check-certification")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_EMAIL));
    }

    @DisplayName("인증 코드 확인 할 때 인증번호는 필수 값 입니다.")
    @Test
    public void checkCertificationWithBlankNumber() throws Exception {
        //given
        CheckCertificationUserRequestDto requestDto = CheckCertificationUserRequestDto.builder()
                .certificationNumber("")
                .email("email@naver.com")
                .build();

        // when
        mockMvc.perform(
                        post("/api/user/check-certification")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_TOKEN));
    }

    @DisplayName("로그아웃")
    @Test
    public void Logout() throws Exception {
        //given
        String validToken = "your-valid-token";
        BDDMockito.doReturn(true)
                .when(authService)
                .logOut(any(String.class));

        //when,then
        mockMvc.perform(post("/api/user/log-out")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").value(true))
                .andDo(document("logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부"),
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지")
                        )
                ));
    }

    @DisplayName("로그아웃 수행시 헤더의 토큰값은 필수 이다.")
    @Test
    public void LogoutWithBlankToken() throws Exception {
        //given
        String headerToken = "";

        //when,then
        mockMvc.perform(post("/api/user/log-out")
                        .header("Authorization", headerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.CERTIFICATION_FAIL))
                .andExpect(jsonPath("$.message").value(ResponseMessage.CERTIFICATION_FAIL))
                .andDo(document("logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("null")
                        )
                ));
    }

    @DisplayName("회원가입 수행.")
    @Test
    public void SignUp() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "이름",
                "123123kim",
                "introduction",
                "imgPath",
                "1234",
                "email@email.com");
        BDDMockito.doReturn(1L).when(authService)
                .singUp(any(SignUpRequestDto.class));

        //when,then
        mockMvc.perform(post("/api/user/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").value(1L))
                .andDo(document("sign-up",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING)
                                        .description("자기소개"),
                                fieldWithPath("imgPath").type(JsonFieldType.STRING)
                                        .description("이미지 주소"),
                                fieldWithPath("certificationNumber").type(JsonFieldType.STRING)
                                        .description("인증번호 4자리")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("유저 id"),
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지")
                        )
                ));
    }

    @DisplayName("회원가입 수행시 이름은 필수 값이다.")
    @Test
    public void SignUpWithBlankName() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "",
                "123123kim",
                "introduction",
                "imgPath",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/user/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_NAME))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 비밀번호 형식에 맞아야 합니다.")
    @Test
    public void SignUpWithBlankPassword() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123",
                "introduction",
                "imgPath",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/user/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 자기소개는 필수 값 입니다.")
    @Test
    public void SignUpWithBlankIntro() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123123kim",
                "",
                "imgPath",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/user/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_INTRO))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 이미지는 필수 값 입니다.")
    @Test
    public void SignUpWithBlankImg() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123123kim",
                "안녕하세요",
                "",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/user/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_IMAGE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 토큰은 필수 값 입니다.")
    @Test
    public void SignUpWithBlankNumber() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123123kim",
                "안녕하세요",
                "img/path",
                "",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/user/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 이메일 형식에 맞아야 합니다.")
    @Test
    public void SignUpWithNotEmail() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123123kim",
                "안녕하세요",
                "img/path",
                "1234",
                "emailemail.com");

        //when,then
        mockMvc.perform(post("/api/user/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_EMAIL))
                .andExpect(status().isBadRequest());
    }

}