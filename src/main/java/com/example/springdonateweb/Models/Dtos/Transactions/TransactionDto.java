package com.example.springdonateweb.Models.Dtos.Transactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private int transactionId;
    private Integer donationId;
    private BigDecimal amount;
    private Integer paymentMethodId;
    private Timestamp transactionDate;
    private String status;
}
