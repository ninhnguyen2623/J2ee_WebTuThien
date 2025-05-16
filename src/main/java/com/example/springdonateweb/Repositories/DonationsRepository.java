package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.DonationsEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationsRepository extends JpaRepository<DonationsEntity, Integer> {
    // Các phương thức tìm kiếm cơ bản
    Page<DonationsEntity> findAll(Pageable pageable);
    
    List<DonationsEntity> findByUserId(int userId);
    
    // Các phương thức tìm kiếm theo tên người quyên góp
    Page<DonationsEntity> findByDonorNameContainingIgnoreCase(String donorName, Pageable pageable);
    
    // Các phương thức tìm kiếm theo ngày
    Page<DonationsEntity> findByDonationDateBetween(LocalDateTime fromDate, LocalDateTime toDate,
                                                    Pageable pageable);
    
    List<DonationsEntity> findByDonationDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
    
    Page<DonationsEntity> findByDonationDateAfter(LocalDateTime fromDate, Pageable pageable);
    
    Page<DonationsEntity> findByDonationDateBefore(LocalDateTime toDate, Pageable pageable);
    
    // Các phương thức tìm kiếm kết hợp
    Page<DonationsEntity> findByDonorNameContainingIgnoreCaseAndDonationDateBetween(
            String donorName, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
    
    Page<DonationsEntity> findByDonorNameContainingIgnoreCaseAndDonationDateAfter(
            String donorName, LocalDateTime fromDate, Pageable pageable);
    
    Page<DonationsEntity> findByDonorNameContainingIgnoreCaseAndDonationDateBefore(
            String donorName, LocalDateTime toDate, Pageable pageable);
    
    @Query("SELECT d.donorName AS donorName, SUM(d.amount) AS totalAmount " +
            "FROM DonationsEntity d " +
            "GROUP BY d.donorName " +
            "ORDER BY totalAmount DESC")
    List<Map<String, Object>> findTopDonors(Pageable pageable);
    
    // Phương thức để lấy tổng số tiền quyên góp theo chương trình
    @Query("SELECT d.programId, SUM(d.amount) " +
            "FROM DonationsEntity d " +
            "GROUP BY d.programId " +
            "ORDER BY d.programId")
    List<Object[]> sumDonationsByProgram();
    
    // Phương thức để đếm số lượng quyên góp theo tháng
    @Query("SELECT FUNCTION('YEAR', d.donationDate), FUNCTION('MONTH', d.donationDate), COUNT(d.donationId) " +
            "FROM DonationsEntity d " +
            "WHERE d.donationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('YEAR', d.donationDate), FUNCTION('MONTH', d.donationDate) " +
            "ORDER BY FUNCTION('YEAR', d.donationDate), FUNCTION('MONTH', d.donationDate)")
    List<Object[]> countDonationsByMonth(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
}
