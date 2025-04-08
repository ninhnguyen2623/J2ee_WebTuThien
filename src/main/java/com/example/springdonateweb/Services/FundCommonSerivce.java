package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.FundCommon.FundCommonResponseDto;
import com.example.springdonateweb.Repositories.FundCommonRepository;
import com.example.springdonateweb.Services.interfaces.IFundCommonService;
import com.example.springdonateweb.Services.mappers.FundCommonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FundCommonSerivce implements IFundCommonService {
    private final FundCommonRepository fundCommonRepository;
    private final FundCommonMapper fundCommonMapper;

    @Override
    public FundCommonResponseDto findById(int id) {
        return fundCommonRepository.findById(id).map(fundCommonMapper::toDto).orElse(null);
    }
}
