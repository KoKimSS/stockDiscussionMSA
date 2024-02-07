package com.example.stockmsastock.domain.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
    @Id
    @GeneratedValue
    private Long id;
    private String basDt; // YYYYMMDD 조회의 기준일, 통상 거래일
    private String srtnCd; // 종목 코드보다 짧으면서 유일성이 보장되는 코드
    private String isinCd; // 국제 채권 식별 번호. 유가증권(채권)의 국제인증 고유번호
    private String mrktCtg; // 시장 구분 (KOSPI/KOSDAQ/KONEX 등)
    private String itmsNm; // 유가증권 국제인증 고유번호 코드 이름
    private String crno; // 종목의 법인등록번호
    private String corpNm; // 종목의 법인 명칭

    @Builder
    private Stock(Long id, String basDt, String srtnCd, String isinCd, String mrktCtg, String itmsNm, String crno, String corpNm) {
        this.id = id;
        this.basDt = basDt;
        this.srtnCd = srtnCd;
        this.isinCd = isinCd;
        this.mrktCtg = mrktCtg;
        this.itmsNm = itmsNm;
        this.crno = crno;
        this.corpNm = corpNm;
    }
}
