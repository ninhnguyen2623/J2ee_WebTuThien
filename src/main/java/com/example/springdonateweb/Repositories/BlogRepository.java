package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Integer>, JpaSpecificationExecutor<BlogEntity> {
    List<BlogEntity> findAll();
    
    Optional<BlogEntity> findById(int id);
    
}
