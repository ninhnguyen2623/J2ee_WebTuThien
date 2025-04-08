package com.example.springdonateweb.Models.Dtos.Categories;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDto {
    @NotNull(message = "Name is required")
    private String name;
    private String description;
}
