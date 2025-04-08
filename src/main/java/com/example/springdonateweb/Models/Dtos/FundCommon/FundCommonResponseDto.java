package com.example.springdonateweb.Models.Dtos.FundCommon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundCommonResponseDto {

    private int id;
    private String name;
    private String note;
    private Integer currentAmount;
}
