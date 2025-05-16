package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.ProgramsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramsRepository extends JpaRepository<ProgramsEntity, Integer> {
    Page<ProgramsEntity> findByStatusTrue(Pageable pageable);
    
    Page<ProgramsEntity> findByStatusFalse(Pageable pageable);
    
    Page<ProgramsEntity> findAll(Pageable pageable);
    
    Optional<ProgramsEntity> findById(int id); //
    
    List<ProgramsEntity> findByCategory_CategoryId(int categoryId);
    
    Page<ProgramsEntity> findByCategory_CategoryId(int categoryId, Pageable pageable);
    
    Optional<ProgramsEntity> findByProgramIdAndStatusTrue(int id); //
    
    // Tìm kiếm theo tên với LIKE (không phân biệt chữ hoa/thường)
    @Query("SELECT p FROM ProgramsEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ProgramsEntity> findByNameContainingIgnoreCase(@Param("search") String search, Pageable pageable);
    
    // Tìm kiếm kết hợp: tên + trạng thái
    @Query("SELECT p FROM ProgramsEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) AND p.status = :status")
    Page<ProgramsEntity> findByNameContainingIgnoreCaseAndStatus(@Param("search") String search,
                                                                 @Param("status") boolean status, Pageable pageable);
    
    // Tìm kiếm kết hợp: tên + danh mục
    @Query("SELECT p FROM ProgramsEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) AND p.category.categoryId = :categoryId")
    Page<ProgramsEntity> findByNameContainingIgnoreCaseAndCategory(@Param("search") String search,
                                                                   @Param("categoryId") int categoryId, Pageable pageable);
    
    // Tìm kiếm kết hợp: danh mục + trạng thái
    @Query("SELECT p FROM ProgramsEntity p WHERE p.category.categoryId = :categoryId AND p.status = :status")
    Page<ProgramsEntity> findByCategoryAndStatus(@Param("categoryId") int categoryId,
                                                 @Param("status") boolean status,
                                                 Pageable pageable);
    
    // Tìm kiếm kết hợp: tên + danh mục + trạng thái
    @Query("SELECT p FROM ProgramsEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) AND p.category.categoryId = :categoryId AND p.status = :status")
    Page<ProgramsEntity> findByNameContainingIgnoreCaseAndCategoryAndStatus(@Param("search") String search,
                                                                            @Param("categoryId") int categoryId, @Param("status") boolean status, Pageable pageable);
}
