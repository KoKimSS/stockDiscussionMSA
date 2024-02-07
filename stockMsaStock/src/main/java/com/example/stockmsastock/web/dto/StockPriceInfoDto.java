package com.example.stockmsastock.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockPriceInfoDto {

    String itmsNm;  //유가증권 국제인증 고유번호 코드 이름
    String mrktCtg; //주식의 시장 구분 (KOSPI/KOSDAQ/KONEX 중 1)
    String clpr; //정규시장의 매매시간종료시까지 형성되는 최종가격
    int vs; // 전일 대비 등락
    int fltRt; // 전일 대비 등락에 따른 비율
    int mkp; // 정규시장의 매매시간개시후 형성되는 최초가격
    int hipr; // 하루 중 가격의 최고치
    int lopr; // 하루 중 가격의 최저치
    int trqu; // 체결수량의 누적 합계
    int trPrc; // 거래건 별 체결가격 * 체결수량의 누적 합계
    int lstgStCnt; // 종목의 상장주식수
    int mrktTotAmt; // 종가 * 상장주식수
    String basDt; // 기준일자
    String srtnCd; // 종목 코드보다 짧으면서 유일성이 보장되는 코드(6자리)
    String isinCd; // 국제 채권 식별 번호. 유가증권(채권)의 국제인증 고유번호

    @Builder
    private StockPriceInfoDto(String itmsNm, String mrktCtg, String clpr, int vs, int fltRt, int mkp, int hipr, int lopr, int trqu, int trPrc, int lstgStCnt, int mrktTotAmt, String basDt, String srtnCd, String isinCd) {
        this.itmsNm = itmsNm;
        this.mrktCtg = mrktCtg;
        this.clpr = clpr;
        this.vs = vs;
        this.fltRt = fltRt;
        this.mkp = mkp;
        this.hipr = hipr;
        this.lopr = lopr;
        this.trqu = trqu;
        this.trPrc = trPrc;
        this.lstgStCnt = lstgStCnt;
        this.mrktTotAmt = mrktTotAmt;
        this.basDt = basDt;
        this.srtnCd = srtnCd;
        this.isinCd = isinCd;
    }
}
