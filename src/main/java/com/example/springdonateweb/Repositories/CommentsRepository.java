package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.CommentsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsEntity, Integer> {
    Page<CommentsEntity> findAll(Pageable pageable);
}
