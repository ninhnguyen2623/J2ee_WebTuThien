package com.example.springdonateweb.Models.Dtos.Transactions;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateDto {
    private int transactionId;

    @NotNull(message = "Donation ID is required")
    private Integer donationId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Payment method ID is required")
    private Integer paymentMethodId;

    private LocalDateTime transactionDate;

    @NotBlank(message = "Status is required")
    private String status;
}
