package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Categories.CategoryCreateDto;
import com.example.springdonateweb.Models.Dtos.Categories.CategoryResponseDto;
import com.example.springdonateweb.Models.Dtos.Categories.CategoryUpdateDto;
import com.example.springdonateweb.Models.Entities.CategoriesEntity;
import com.example.springdonateweb.Repositories.CategoriesRepository;
import com.example.springdonateweb.Services.interfaces.ICategoriesService;
import com.example.springdonateweb.Services.mappers.CategoriesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService implements ICategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    @Override
    public List<CategoryResponseDto> findAll() {
        return categoriesRepository.findAll().stream()
                .map(categoriesMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public Page<CategoryResponseDto> findCategoriesByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoriesEntity> categoryPage = categoriesRepository.findAll(pageable);
        return categoryPage.map(categoriesMapper::toDto);
    }
    @Override
    public CategoryResponseDto findById(int id) {
        return categoriesRepository.findById(id)
                .map(categoriesMapper::toDto)
                .orElse(null);
    }
    @Override
    public List<CategoriesEntity> findAll2() {
        return categoriesRepository.findAll(); // Trả về danh sách CategoriesEntity trực tiếp
    }
    @Override
    public CategoryResponseDto create(CategoryCreateDto categoryCreateDto) {
        CategoriesEntity categoriesEntity = categoriesMapper.toEntity(categoryCreateDto);
        CategoriesEntity savedCategory = categoriesRepository.save(categoriesEntity);
        return categoriesMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponseDto update(int id, CategoryUpdateDto categoryUpdateDto) {
        return categoriesRepository.findById(id)
                .map(existingCategory -> {
                    CategoriesEntity updatedCategory = categoriesMapper.partialUpdate(categoryUpdateDto, existingCategory);
                    return categoriesMapper.toDto(categoriesRepository.save(updatedCategory));
                })
                .orElse(null);
    }

    @Override
    public void delete(int id) {
        categoriesRepository.deleteById(id);
    }
}
