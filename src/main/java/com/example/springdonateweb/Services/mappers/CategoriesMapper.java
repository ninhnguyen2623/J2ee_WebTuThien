package com.example.springdonateweb.Services.mappers;

import com.example.springdonateweb.Models.Dtos.Categories.CategoryCreateDto;
import com.example.springdonateweb.Models.Dtos.Categories.CategoryResponseDto;
import com.example.springdonateweb.Models.Dtos.Categories.CategoryUpdateDto;
import com.example.springdonateweb.Models.Entities.CategoriesEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {
    CategoriesEntity toEntity(CategoryCreateDto categoryCreateDto);
    CategoriesEntity toEntity(CategoryUpdateDto categoryUpdateDto);
    CategoryResponseDto toDto(CategoriesEntity categoriesEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CategoriesEntity partialUpdate(CategoryUpdateDto categoryUpdateDto, @MappingTarget CategoriesEntity categoriesEntity);
}
