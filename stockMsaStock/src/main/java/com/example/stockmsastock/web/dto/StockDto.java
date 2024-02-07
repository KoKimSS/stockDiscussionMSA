package com.example.stockmsastock.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDto {
    private String cd; //코드
    private String nm; //이름
    private String sv; //전일 가
    private String nv; //현재가
    private String cv; //전일 대비 가격
    private String cr; //전일 대비 퍼센트
    private String rf; //통화 코드
    private String mt; //시장 구분 kospi : 1, kosdaq : 2
    private String ms;
    private String tyn;
    private String pcv;
    private String ov;
    private String hv;
    private String lv;
    private String ul;
    private String ll;
    private String aq;
    private String aa;
    private String nav;
    private String keps;
    private String eps;
    private String bps;
    private String cnsEps;


}
