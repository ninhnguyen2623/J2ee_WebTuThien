package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.PaymentmethodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentmethodsEntity, Integer> {
    Page<PaymentmethodsEntity> findAll(Pageable pageable);
}