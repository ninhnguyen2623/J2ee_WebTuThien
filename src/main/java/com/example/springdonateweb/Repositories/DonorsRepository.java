package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.DonorsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonorsRepository extends JpaRepository<DonorsEntity, Integer> {
    Page<DonorsEntity> findAll(Pageable pageable);
}
