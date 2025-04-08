package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.FundCommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundCommonRepository extends JpaRepository<FundCommonEntity, Integer> {
    Optional<FundCommonEntity> findById(int id);
}