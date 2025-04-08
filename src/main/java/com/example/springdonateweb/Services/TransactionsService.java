package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Transactions.TransactionCreateDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionResponseDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionUpdateDto;
import com.example.springdonateweb.Models.Entities.TransactionsEntity;
import com.example.springdonateweb.Repositories.TransactionsRepository;
import com.example.springdonateweb.Services.interfaces.ITransactionsService;
import com.example.springdonateweb.Services.mappers.TransactionsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionsService implements ITransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final TransactionsMapper transactionsMapper;

    @Override
    public List<TransactionResponseDto> findAll() {
        return transactionsRepository.findAll().stream()
                .map(transactionsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponseDto findById(int id) {
        return transactionsRepository.findById(id)
                .map(transactionsMapper::toDto)
                .orElse(null);
    }
    @Override
    public Page<TransactionResponseDto> findTransactionsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionsEntity> transactionsPage = transactionsRepository.findAll(pageable);
        return transactionsPage.map(transactionsMapper::toDto);
    }
    @Override
    public TransactionResponseDto create(TransactionCreateDto transactionCreateDto) {
        TransactionsEntity transactionsEntity = transactionsMapper.toEntity(transactionCreateDto);
        TransactionsEntity savedTransaction = transactionsRepository.save(transactionsEntity);
        return transactionsMapper.toDto(savedTransaction);
    }

    @Override
    public TransactionResponseDto update(int id, TransactionUpdateDto transactionUpdateDto) {
        return transactionsRepository.findById(id)
                .map(existingTransaction -> {
                    TransactionsEntity updatedTransaction = transactionsMapper.partialUpdate(transactionUpdateDto, existingTransaction);
                    return transactionsMapper.toDto(transactionsRepository.save(updatedTransaction));
                })
                .orElse(null);
    }

    @Override
    public void delete(int id) {
        transactionsRepository.deleteById(id);
    }
}
