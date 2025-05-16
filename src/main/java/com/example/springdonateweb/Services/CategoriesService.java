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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

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
    public Page<CategoryResponseDto> findCategoriesByPage(int page, int size, String keyword, String sortBy,
                                                          String sortDir) {
        Sort sort = Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CategoriesEntity> categoryPage;
        if (keyword != null && !keyword.isEmpty()) {
            categoryPage = categoriesRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            categoryPage = categoriesRepository.findAll(pageable);
        }
        
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
        return categoriesRepository.findAll();
    }
    
    @Override
    public Map<String, Double> getTotalDonationsByCategory() {
        try {
            // Create a map to store category name and donation amount
            Map<String, Double> result = new HashMap<>();
            
            // Get all categories
            List<CategoryResponseDto> categories = findAll();
            
            // For each category, sum the donations from programs in that category
            for (CategoryResponseDto category : categories) {
                // Get the total donations for this category from programs in this category
                Double totalAmount = categoriesRepository.sumDonationsByCategoryId(category.getCategoryId());
                
                // If there are donations for this category, add to the result
                if (totalAmount != null && totalAmount > 0) {
                    result.put(category.getName(), totalAmount);
                } else {
                    // If no donations, put 0
                    result.put(category.getName(), 0.0);
                }
            }
            
            return result;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error getting donations by category: " + e.getMessage());
            e.printStackTrace();
            
            // Return empty map in case of error
            return new HashMap<>();
        }
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
                                       CategoriesEntity updatedCategory = categoriesMapper.partialUpdate(categoryUpdateDto,
                                                                                                         existingCategory);
                                       return categoriesMapper.toDto(categoriesRepository.save(updatedCategory));
                                   })
                                   .orElse(null);
    }
    
    @Override
    public void delete(int id) {
        categoriesRepository.deleteById(id);
    }
}
