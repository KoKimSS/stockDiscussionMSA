package com.example.stockmsastock.web.controller;

import com.example.stockmsastock.common.error.ResponseCode;
import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.repository.StockSortOrder;
import com.example.stockmsastock.repository.StockSortType;
import com.example.stockmsastock.restdocs.AbstractRestDocsTests;
import com.example.stockmsastock.service.StockService;
import com.example.stockmsastock.web.dto.StockDto;
import com.example.stockmsastock.web.dto.StockPageDto;
import com.example.stockmsastock.web.dto.request.FindByItemCodeRequestDto;
import com.example.stockmsastock.web.dto.request.FindByNameRequestDto;
import com.example.stockmsastock.web.dto.request.GetStockPageOrderByRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static com.example.stockmsastock.common.error.ResponseMessage.SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {StockController.class})
class StockControllerTest extends AbstractRestDocsTests {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StockService stockService;

    @DisplayName("이름이 포함된 스톡 찾기")
    @Test
    public void findByName() throws Exception {
        //given
        String stockName = "전자";

        StockDto SamSung = StockDto.builder()
                .stockName("삼성전자")
                .id(1L)
                .itemCode("005930")
                .accumulatedTradingValue(1173317L)
                .accumulatedTradingVolume(15999606L)
                .fluctuationsRatio(-0.27D)
                .category("KOSPI").build();
        StockDto SamJi = StockDto.builder()
                .stockName("삼지전자")
                .id(2L)
                .itemCode("037460")
                .accumulatedTradingValue(251L)
                .accumulatedTradingVolume(29299L)
                .fluctuationsRatio(-1.16D)
                .category("KOSDAQ").build();
        List<StockDto> stockDtoList = List.of(SamSung, SamJi);
        BDDMockito.doReturn(stockDtoList)
                .when(stockService)
                .findByName(any(FindByNameRequestDto.class));

        FindByNameRequestDto requestDto = FindByNameRequestDto.builder()
                .name(stockName)
                .build();

        // when
        mockMvc.perform(
                        post("/api/stock/find-by-name")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("create-likes",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("종목 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                                        .description("id"),
                                fieldWithPath("data[].stockName").type(JsonFieldType.STRING)
                                        .description("종목 이름"),
                                fieldWithPath("data[].itemCode").type(JsonFieldType.STRING)
                                        .description("종목 코드"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING)
                                        .description("카테고리"),
                                fieldWithPath("data[].accumulatedTradingVolume").type(JsonFieldType.NUMBER)
                                        .description("전일 누적 거래량"),
                                fieldWithPath("data[].accumulatedTradingValue").type(JsonFieldType.NUMBER)
                                        .description("전일 누적 거래대금"),
                                fieldWithPath("data[].fluctuationsRatio").type(JsonFieldType.NUMBER)
                                        .description("전일 상승률")
                        )
                ));
    }
    @DisplayName("코드로 스톡 찾기")
    @Test
    public void findByItemCode() throws Exception {
        //given
        String itemCode = "005930";

        StockDto SamSung = StockDto.builder()
                .stockName("삼성전자")
                .id(1L)
                .itemCode(itemCode)
                .accumulatedTradingValue(1173317L)
                .accumulatedTradingVolume(15999606L)
                .fluctuationsRatio(-0.27D)
                .category("KOSPI").build();

        BDDMockito.doReturn(SamSung)
                .when(stockService)
                .findByItemCode(any(FindByItemCodeRequestDto.class));

        FindByItemCodeRequestDto requestDto = FindByItemCodeRequestDto.builder()
                .itemCode(itemCode)
                .build();

        // when
        mockMvc.perform(
                        post("/api/stock/find-by-itemCode")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").value(SamSung))
                .andDo(document("create-likes",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("itemCode").type(JsonFieldType.STRING)
                                        .description("종목 코드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("id"),
                                fieldWithPath("data.stockName").type(JsonFieldType.STRING)
                                        .description("종목 이름"),
                                fieldWithPath("data.itemCode").type(JsonFieldType.STRING)
                                        .description("종목 코드"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING)
                                        .description("카테고리"),
                                fieldWithPath("data.accumulatedTradingVolume").type(JsonFieldType.NUMBER)
                                        .description("전일 누적 거래량"),
                                fieldWithPath("data.accumulatedTradingValue").type(JsonFieldType.NUMBER)
                                        .description("전일 누적 거래대금"),
                                fieldWithPath("data.fluctuationsRatio").type(JsonFieldType.NUMBER)
                                        .description("전일 상승률")
                        )
                ));
    }

    @DisplayName("정렬된 stock page 얻기")
    @Test
    public void getPageOrderBy() throws Exception {
        //given

        StockSortType name = StockSortType.NAME;
        StockSortOrder asc = StockSortOrder.ASC;

        StockDto SamSung = StockDto.builder()
                .stockName("삼성전자")
                .id(1L)
                .itemCode("005930")
                .accumulatedTradingValue(1173317L)
                .accumulatedTradingVolume(15999606L)
                .fluctuationsRatio(-0.27D)
                .category("KOSPI").build();
        StockDto SamJi = StockDto.builder()
                .stockName("삼지전자")
                .id(2L)
                .itemCode("037460")
                .accumulatedTradingValue(251L)
                .accumulatedTradingVolume(29299L)
                .fluctuationsRatio(-1.16D)
                .category("KOSDAQ").build();

        StockPageDto stockPageDto = StockPageDto.builder()
                .totalPages(1)
                .totalElements(2)
                .content(List.of(SamSung, SamJi))
                .size(2)
                .numberOfElements(2).build();

        GetStockPageOrderByRequestDto requestDto = GetStockPageOrderByRequestDto.builder()
                .page(0)
                .size(2)
                .sortBy(name)
                .sortOrder(asc)
                .build();

        BDDMockito.doReturn(stockPageDto)
                .when(stockService)
                .getPageOrderBy(any(StockSortType.class),any(StockSortOrder.class),any(Pageable.class));


        // when
        mockMvc.perform(
                        post("/api/stock/find-page-orderBy")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("create-likes",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("page").type(JsonFieldType.NUMBER)
                                        .description("페이지"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("사이즈"),
                                fieldWithPath("sortBy").type(JsonFieldType.STRING)
                                        .description("정렬 값"),
                                fieldWithPath("sortOrder").type(JsonFieldType.STRING)
                                        .description("정렬 종류")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                        .description("콘텐츠"),
                                fieldWithPath("data.content[].stockName").type(JsonFieldType.STRING)
                                        .description("스톡 이름"),
                                fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                                        .description("스톡 아이디"),
                                fieldWithPath("data.content[].itemCode").type(JsonFieldType.STRING)
                                        .description("스톡 코드"),
                                fieldWithPath("data.content[].accumulatedTradingValue").type(JsonFieldType.NUMBER)
                                        .description("전일 누적 거래량"),
                                fieldWithPath("data.content[].accumulatedTradingVolume").type(JsonFieldType.NUMBER)
                                        .description("전일 누적 거래대금"),
                                fieldWithPath("data.content[].fluctuationsRatio").type(JsonFieldType.NUMBER)
                                        .description("전일 상승률"),
                                fieldWithPath("data.content[].category").type(JsonFieldType.STRING)
                                        .description("카테고리"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                        .description("종목 이름"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                        .description("종목 코드"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("카테고리"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("전일 누적 거래량")
                        )
                ));
    }
}