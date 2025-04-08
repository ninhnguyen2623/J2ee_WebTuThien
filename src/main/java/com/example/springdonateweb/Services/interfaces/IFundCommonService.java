package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.FundCommon.FundCommonResponseDto;

public interface IFundCommonService {
    FundCommonResponseDto findById(int id);
}
