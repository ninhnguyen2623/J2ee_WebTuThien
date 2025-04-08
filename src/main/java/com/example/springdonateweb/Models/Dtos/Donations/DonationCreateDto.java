package com.example.springdonateweb.Models.Dtos.Donations;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class DonationCreateDto {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer programId;
    @NotNull
    private BigDecimal amount;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime donationDate; // Giữ nguyên kiểu Timestamp
    private String donorName;
}
