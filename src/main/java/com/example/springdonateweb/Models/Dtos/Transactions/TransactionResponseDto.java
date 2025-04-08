package com.example.springdonateweb.Models.Dtos.Transactions;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private int transactionId;
    private Integer donationId;
    private BigDecimal amount;
    private Integer paymentMethodId;
    private LocalDateTime transactionDate;
    private String status;
}
