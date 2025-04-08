package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.TransactionsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<TransactionsEntity, Integer> {
    Page<TransactionsEntity> findAll(Pageable pageable);
}
