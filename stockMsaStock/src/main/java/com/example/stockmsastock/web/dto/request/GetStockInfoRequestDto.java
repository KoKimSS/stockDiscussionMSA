package com.example.stockmsastock.web.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetStockInfoRequestDto {

    private int numOfRows; // 한 페이지 결과 수
    private int pageNo; // 페이지 번호
    private String resultType; // 구분(xml, json) Default: xml
    private String basDt; // 검색값과 기준일자가 일치하는 데이터를 검색 ex) 240207 6자리 String
    private String beginBasDt; // 기준일자가 검색값보다 크거나 같은 데이터를 검색
    private String endBasDt; // 기준일자가 검색값보다 작은 데이터를 검색
    private String likeBasDt; // 기준일자값이 검색값을 포함하는 데이터를 검색
    private String likeSrtnCd; // 단축코드가 검색값을 포함하는 데이터를 검색
    private String isinCd; // 검색값과 ISIN코드이 일치하는 데이터를 검색
    private String likeIsinCd; // ISIN코드가 검색값을 포함하는 데이터를 검색
    private String itmsNm; // 검색값과 종목명이 일치하는 데이터를 검색
    private String likeItmsNm; // 	종목명이 검색값을 포함하는 데이터를 검색
    private String crno; // 검색값과 법인등록번호가 일치하는 데이터를 검색
    private String corpNm; // 검색값과 법인명이 일치하는 데이터를 검색
    private String likeCorpNm; // 법인명이 검색값을 포함하는 데이터를 검색

    @Builder
    private GetStockInfoRequestDto(int numOfRows, int pageNo, String resultType, String basDt, String beginBasDt, String endBasDt, String likeBasDt, String likeSrtnCd, String isinCd, String likeIsinCd, String itmsNm, String likeItmsNm, String crno, String corpNm, String likeCorpNm) {
        this.numOfRows = numOfRows;
        this.pageNo = pageNo;
        this.resultType = resultType;
        this.basDt = basDt;
        this.beginBasDt = beginBasDt;
        this.endBasDt = endBasDt;
        this.likeBasDt = likeBasDt;
        this.likeSrtnCd = likeSrtnCd;
        this.isinCd = isinCd;
        this.likeIsinCd = likeIsinCd;
        this.itmsNm = itmsNm;
        this.likeItmsNm = likeItmsNm;
        this.crno = crno;
        this.corpNm = corpNm;
        this.likeCorpNm = likeCorpNm;
    }
}

