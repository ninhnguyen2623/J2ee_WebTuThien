package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.Transactions.TransactionCreateDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionResponseDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITransactionsService {
    List<TransactionResponseDto> findAll();
    TransactionResponseDto findById(int id);
    TransactionResponseDto create(TransactionCreateDto transactionCreateDto);
    TransactionResponseDto update(int id, TransactionUpdateDto transactionUpdateDto);
    void delete(int id);
    Page<TransactionResponseDto> findTransactionsByPage(int page, int size);
}
