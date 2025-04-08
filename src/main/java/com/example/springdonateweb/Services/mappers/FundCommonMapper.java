package com.example.springdonateweb.Services.mappers;

import org.mapstruct.*;

import com.example.springdonateweb.Models.Dtos.FundCommon.FundCommonResponseDto;
import com.example.springdonateweb.Models.Entities.FundCommonEntity;

@Mapper(componentModel = "spring")
public interface FundCommonMapper {
    
    FundCommonResponseDto toDto(FundCommonEntity fundCommonEntity);
    FundCommonEntity toEntity(FundCommonResponseDto fundCommonResponseDto);


}