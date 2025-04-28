package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.CategoriesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
    // Additional query methods if needed
    Page<CategoriesEntity> findAll(Pageable pageable);
    
    // Tìm kiếm theo name hoặc description chứa từ khóa
    Page<CategoriesEntity> findByNameContainingOrDescriptionContaining(String name, String description,
                                                                       Pageable pageable);
    
    // Tìm kiếm chỉ theo tên danh mục
    Page<CategoriesEntity> findByNameContaining(String name, Pageable pageable);
    
    // Tìm kiếm không phân biệt chữ hoa/thường (tương thích với nhiều cơ sở dữ liệu)
    @Query("SELECT c FROM CategoriesEntity c WHERE LOWER(c.name) LIKE LOWER(concat('%', :name, '%'))")
    Page<CategoriesEntity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}
