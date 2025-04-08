package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.CategoriesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
    // Additional query methods if needed
    Page<CategoriesEntity> findAll(Pageable pageable);
}
