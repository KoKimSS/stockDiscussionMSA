package com.example.stockmsaactivity.web.api.dto.request.stock;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindByItemCodeRequestDto {

    private String itemCode;

}
