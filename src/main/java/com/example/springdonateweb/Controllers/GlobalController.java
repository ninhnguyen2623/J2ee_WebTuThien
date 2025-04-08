package com.example.springdonateweb.Controllers;


import com.example.springdonateweb.Models.Dtos.Categories.CategoryResponseDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Services.CategoriesService;
import com.example.springdonateweb.Services.ProgramsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class GlobalController {
    private final CategoriesService categoriesService;
    private final ProgramsService programsService;

    @ModelAttribute("category")
    public List<CategoryResponseDto> categories() {
        return categoriesService.findAll();
    }
    



}
