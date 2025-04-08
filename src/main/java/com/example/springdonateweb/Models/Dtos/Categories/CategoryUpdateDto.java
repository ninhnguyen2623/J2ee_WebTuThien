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
public class CategoryUpdateDto {
    @NotNull(message = "Category ID is required")
    private Integer categoryId;
    @NotNull(message = "Name is required")
    private String name;
    private String description;
}
