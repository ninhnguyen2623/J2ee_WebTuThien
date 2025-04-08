package com.example.springdonateweb.Models.Dtos.Donors;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DonorCreateDto {
    private Integer programId;
    private Integer userId;
    private Integer donationId;
    private LocalDateTime donationDate;
}
