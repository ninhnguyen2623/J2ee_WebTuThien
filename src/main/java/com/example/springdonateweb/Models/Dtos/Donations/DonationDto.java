package com.example.springdonateweb.Models.Dtos.Donations;

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
public class DonationDto {
    private int donationId;
    private Integer userId;
    private Integer programId;
    private BigDecimal amount;
    private LocalDateTime donationDate;
    private String donorName;
}
