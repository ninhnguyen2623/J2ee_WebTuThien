package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Categories.CategoryCreateDto;
import com.example.springdonateweb.Models.Dtos.Categories.CategoryResponseDto;
import com.example.springdonateweb.Models.Dtos.Categories.CategoryUpdateDto;
import com.example.springdonateweb.Services.interfaces.ICategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoriesController {

    private final ICategoriesService categoriesService;

    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CategoryResponseDto> categoryPage = categoriesService.findCategoriesByPage(page, size);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        return "admin/Categories/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category", new CategoryCreateDto());
        return "admin/Categories/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CategoryCreateDto categoryCreateDto) {
        categoriesService.create(categoryCreateDto);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        CategoryResponseDto category = categoriesService.findById(id);
        if (category == null) return "redirect:/admin/categories";
        model.addAttribute("category", category);
        return "admin/Categories/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute CategoryUpdateDto categoryUpdateDto) {
        categoriesService.update(id, categoryUpdateDto);
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        categoriesService.delete(id);
        return "redirect:/admin/categories";
    }
}
