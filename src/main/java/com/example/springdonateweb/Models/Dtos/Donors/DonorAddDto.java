package com.example.springdonateweb.Models.Dtos.Donors;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorAddDto {
    @NotNull(message = "Program ID is required")
    private Integer programId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Donation ID is required")
    private Integer donationId;

    private LocalDateTime donationDate;
}
