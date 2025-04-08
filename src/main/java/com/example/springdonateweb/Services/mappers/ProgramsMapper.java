package com.example.springdonateweb.Services.mappers;

import com.example.springdonateweb.Models.Dtos.Programs.ProgramCreateDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramUpdateDto;
import com.example.springdonateweb.Models.Entities.CategoriesEntity;
import com.example.springdonateweb.Models.Entities.ProgramsEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProgramsMapper {
    @Mappings({
        @Mapping(source = "image", target = "image", qualifiedByName = "multipartFileToString")
    })
    ProgramsEntity toEntity(ProgramCreateDto programsCreateDto);
    ProgramsEntity toEntity(ProgramUpdateDto programsUpdateDto);

     @Mappings({
            @Mapping(target = "percentageAchieved", expression = "java(calculatePercentage(entity.getCurrentAmount(), entity.getGoalAmount()))"),
            @Mapping(source = "category", target = "category", qualifiedByName = "categoryToString"),
            @Mapping(target = "startDate", expression = "java(convertDateToString(entity.getStartDate()))"),
            @Mapping(target = "endDate", expression = "java(convertDateToString(entity.getEndDate()))"),
            @Mapping(target = "remainingDays", expression = "java(calculateRemainingDays(convertDateToString(entity.getStartDate()), convertDateToString(entity.getEndDate())))")
    })
     ProgramsResponseDto toDto(ProgramsEntity entity);

    // Mapping method to extract the category name from CategoriesEntity
    @Named("categoryToString")
    default String mapCategoryToString(CategoriesEntity category) {
        return category != null ? category.getName() : null;
    }
    default int calculatePercentage(Integer currentAmount, Integer goalAmount) {
        if (goalAmount != null && goalAmount > 0 && currentAmount != null) {
            return (currentAmount * 100) / goalAmount;
        }
        return 0;
    }
    default long calculateRemainingDays(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        // Calculate the difference in days by converting to epoch days
        return end.toEpochDay() - start.toEpochDay();
    }
    default String convertDateToString(Date sqlDate) {
        return sqlDate != null ? sqlDate.toLocalDate().toString() : null;
    }

    @Named("multipartFileToString")
    default String multipartFileToString(org.springframework.web.multipart.MultipartFile file) {
        return file != null ? file.getOriginalFilename() : null;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProgramsEntity partialUpdate(ProgramUpdateDto programsUpdateDto, @MappingTarget ProgramsEntity programsEntity);
}
