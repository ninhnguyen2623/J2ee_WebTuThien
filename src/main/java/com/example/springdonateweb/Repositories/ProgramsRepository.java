package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.ProgramsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProgramsRepository extends JpaRepository<ProgramsEntity, Integer> {
    Page<ProgramsEntity> findByStatusTrue(Pageable pageable);

    Page<ProgramsEntity> findAll(Pageable pageable);

    Optional<ProgramsEntity> findById(int id); //

    
    List<ProgramsEntity> findByCategory_CategoryId(int categoryId); 

    Optional<ProgramsEntity> findByProgramIdAndStatusTrue(int id); //



}
