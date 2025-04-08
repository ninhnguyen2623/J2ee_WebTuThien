package com.example.springdonateweb.Services.mappers;

import com.example.springdonateweb.Models.Dtos.Transactions.TransactionCreateDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionResponseDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionUpdateDto;
import com.example.springdonateweb.Models.Entities.TransactionsEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransactionsMapper {

    TransactionsEntity toEntity(TransactionCreateDto transactionCreateDto);

    TransactionsEntity toEntity(TransactionUpdateDto transactionUpdateDto);

    TransactionResponseDto toDto(TransactionsEntity transactionsEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransactionsEntity partialUpdate(TransactionUpdateDto transactionUpdateDto, @MappingTarget TransactionsEntity transactionsEntity);
}
