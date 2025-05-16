package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.Donations.DonationCreateDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IDonationsService {
    List<DonationResponseDto> findAll();
    
    DonationResponseDto findById(int id);
    
    DonationResponseDto create(DonationCreateDto donationCreateDto);
    
    DonationResponseDto update(int id, DonationUpdateDto donationUpdateDto);
    
    void delete(int id);
    
    Page<DonationResponseDto> findDonationsByPage(int page, int size);
    
    // Phương thức hỗ trợ sắp xếp với Pageable
    Page<DonationResponseDto> findDonationsByPage(Pageable pageable);
    
    Page<DonationResponseDto> searchDonations(String searchTerm, LocalDate fromDate, LocalDate toDate, int page,
                                              int size);
    
    List<DonationResponseDto> findByUserId(int userId);
    
    Map<String, BigDecimal> getTotalDonationsByDay();
    
    List<Map<String, Object>> getTopDonors(int limit);
    
    Map<Integer, BigDecimal> getTotalDonationsByProgram();
    
    /**
     * Lấy tổng số tiền quyên góp theo từng chương trình
     *
     * @return Map với key là tên chương trình và value là tổng số tiền
     */
    Map<String, Double> getTotalDonationByProgram();
    
    /**
     * Lấy số lượng quyên góp theo các tháng gần đây
     *
     * @param months Số tháng cần lấy dữ liệu
     * @return Map với key là tên tháng và value là số lượng quyên góp
     */
    Map<String, Integer> getDonationCountsByMonth(int months);
}
