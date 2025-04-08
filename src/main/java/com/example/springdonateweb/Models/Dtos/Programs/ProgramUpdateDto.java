package com.example.springdonateweb.Models.Dtos.Programs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramUpdateDto {
    private int programId;
    private String name;
    private String description;
    private Integer goalAmount;
    private String image;
    private Date startDate;
    private Date endDate;
    private boolean status;
    private int categoryId;

}
