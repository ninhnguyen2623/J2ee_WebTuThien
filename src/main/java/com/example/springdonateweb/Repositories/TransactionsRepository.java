package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.TransactionsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionsRepository extends JpaRepository<TransactionsEntity, Integer> {
    Page<TransactionsEntity> findAll(Pageable pageable);
    
    @Query("SELECT t FROM TransactionsEntity t WHERE " +
            "(:status IS NULL OR t.status LIKE %:status%) AND " +
            "(:dateFrom IS NULL OR t.transactionDate >= :dateFrom) AND " +
            "(:dateTo IS NULL OR t.transactionDate <= :dateTo)")
    Page<TransactionsEntity> findByFilters(
            @Param("status") String status,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable);
}
