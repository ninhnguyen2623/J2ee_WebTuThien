package com.example.springdonateweb.Models.Dtos.Transactions;

import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private int id;
    private Integer donationId;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String status;
    private String description;
    private ProgramsResponseDto program;
}
