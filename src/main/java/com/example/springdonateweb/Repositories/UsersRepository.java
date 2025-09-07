package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByEmail(String email);
    
    // Optional<UsersEntity> findByIdAndStatusTrue(Integer id);
    boolean existsByEmail(String email);
    
    List<UsersEntity> findByStatusTrue();
    
    // Search methods
    Page<UsersEntity> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email,
                                                                                Pageable pageable);
    
    Page<UsersEntity> findByRoleId(Integer roleId, Pageable pageable);
    
    Page<UsersEntity> findByStatus(Boolean status, Pageable pageable);
    
    @Query("SELECT u FROM UsersEntity u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "u.roleId = :roleId")
    Page<UsersEntity> findByNameOrEmailAndRoleId(
            @Param("search") String search,
            @Param("roleId") Integer roleId,
            Pageable pageable);
    
    @Query("SELECT u FROM UsersEntity u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "u.status = :status")
    Page<UsersEntity> findByNameOrEmailAndStatus(
            @Param("search") String search,
            @Param("status") Boolean status,
            Pageable pageable);
    
    @Query("SELECT u FROM UsersEntity u WHERE " +
            "u.roleId = :roleId AND u.status = :status")
    Page<UsersEntity> findByRoleIdAndStatus(
            @Param("roleId") Integer roleId,
            @Param("status") Boolean status,
            Pageable pageable);
    
    @Query("SELECT u FROM UsersEntity u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "u.roleId = :roleId AND u.status = :status")
    Page<UsersEntity> findByNameOrEmailAndRoleIdAndStatus(
            @Param("search") String search,
            @Param("roleId") Integer roleId,
            @Param("status") Boolean status,
            Pageable pageable);
    
    Optional<UsersEntity> findByIdAndStatusTrue(int id);
    
    // boolean existsByIdAndStatusTrue(Integer id);
    Optional<UsersEntity> findByEmailAndStatusTrue(String email);
}
