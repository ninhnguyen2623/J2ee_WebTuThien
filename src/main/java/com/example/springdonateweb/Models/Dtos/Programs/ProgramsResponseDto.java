package com.example.springdonateweb.Models.Dtos.Programs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramsResponseDto {
    
    private int programId;
    private String name;
    private String description;
    private String image;
    private Integer donationCount;
    private String startDate;
    private String endDate;
    private boolean status;
    private Integer goalAmount;
    private Integer currentAmount;
    private String category;
    private Integer categoryId;
    
    private int percentageAchieved;
    private long remainingDays;
}
