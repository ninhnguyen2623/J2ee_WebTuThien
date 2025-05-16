package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionCreateDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionResponseDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionUpdateDto;
import com.example.springdonateweb.Models.Entities.PaymentmethodsEntity;
import com.example.springdonateweb.Models.Entities.ProgramsEntity;
import com.example.springdonateweb.Models.Entities.TransactionsEntity;
import com.example.springdonateweb.Repositories.DonationsRepository;
import com.example.springdonateweb.Repositories.PaymentMethodRepository;
import com.example.springdonateweb.Repositories.ProgramsRepository;
import com.example.springdonateweb.Repositories.TransactionsRepository;
import com.example.springdonateweb.Services.interfaces.ITransactionsService;
import com.example.springdonateweb.Services.mappers.ProgramsMapper;
import com.example.springdonateweb.Services.mappers.TransactionsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionsService implements ITransactionsService {
    
    private final TransactionsRepository transactionsRepository;
    private final TransactionsMapper transactionsMapper;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProgramsRepository programsRepository;
    private final ProgramsMapper programsMapper;
    private final DonationsRepository donationsRepository;
    
    @Override
    public List<TransactionResponseDto> findAll() {
        return transactionsRepository.findAll().stream()
                                     .map(this::convertToDto)
                                     .collect(Collectors.toList());
    }
    
    @Override
    public TransactionResponseDto findById(int id) {
        return transactionsRepository.findById(id)
                                     .map(this::convertToDto)
                                     .orElse(null);
    }
    
    @Override
    public Page<TransactionResponseDto> findTransactionsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionsEntity> transactionsPage = transactionsRepository.findAll(pageable);
        return transactionsPage.map(this::convertToDto);
    }
    
    @Override
    public Page<TransactionResponseDto> findTransactionsByFilters(
            String searchTerm, LocalDate dateFrom, LocalDate dateTo, int page, int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        // Convert LocalDate to LocalDateTime for the repository query
        LocalDateTime dateTimeFrom = dateFrom != null ? LocalDateTime.of(dateFrom, LocalTime.MIN) : null;
        
        LocalDateTime dateTimeTo = dateTo != null ? LocalDateTime.of(dateTo, LocalTime.MAX) : null;
        
        Page<TransactionsEntity> transactionsPage = transactionsRepository.findByFilters(
                searchTerm, dateTimeFrom, dateTimeTo, pageable);
        
        return transactionsPage.map(this::convertToDto);
    }
    
    private TransactionResponseDto convertToDto(TransactionsEntity entity) {
        TransactionResponseDto dto = transactionsMapper.toDto(entity);
        
        // Set payment method name
        if (entity.getPaymentMethodId() != null) {
            Optional<PaymentmethodsEntity> paymentMethod = paymentMethodRepository
                    .findById(entity.getPaymentMethodId());
            paymentMethod.ifPresent(method -> dto.setPaymentMethod(method.getMethodName()));
        }
        
        // Set description
        // This is a placeholder, adjust according to your business logic
        dto.setDescription(entity.getStatus() != null
                                   ? "Giao dịch " + entity.getStatus().toLowerCase() + " #" + entity.getTransactionId()
                                   : "Giao dịch #" + entity.getTransactionId());
        
        // Set program info if applicable - based on donation relationship
        if (entity.getDonationId() != null) {
            // Get donation from repository
            donationsRepository.findById(entity.getDonationId()).ifPresent(donation -> {
                // If donation has program ID, get program details
                if (donation.getProgramId() != null) {
                    programsRepository.findById(donation.getProgramId()).ifPresent(programEntity -> {
                        dto.setProgram(programsMapper.toDto(programEntity));
                    });
                }
            });
        }
        
        return dto;
    }
    
    @Override
    public TransactionResponseDto create(TransactionCreateDto transactionCreateDto) {
        TransactionsEntity transactionsEntity = transactionsMapper.toEntity(transactionCreateDto);
        TransactionsEntity savedTransaction = transactionsRepository.save(transactionsEntity);
        return convertToDto(savedTransaction);
    }
    
    @Override
    public TransactionResponseDto update(int id, TransactionUpdateDto transactionUpdateDto) {
        return transactionsRepository.findById(id)
                                     .map(existingTransaction -> {
                                         TransactionsEntity updatedTransaction = transactionsMapper.partialUpdate(transactionUpdateDto,
                                                                                                                  existingTransaction);
                                         return convertToDto(transactionsRepository.save(updatedTransaction));
                                     })
                                     .orElse(null);
    }
    
    @Override
    public void delete(int id) {
        transactionsRepository.deleteById(id);
    }
}
