package com.example.springdonateweb.Models.Dtos.Donations;

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
public class DonationUpdateDto {
    @NotNull(message = "Donation ID is required")
    private Integer donationId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Program ID is required")
    private Integer programId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private LocalDateTime donationDate;

    private String donorName;
}
