package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.Donations.DonationCreateDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationUpdateDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IDonationsService {
    List<DonationResponseDto> findAll();
    DonationResponseDto findById(int id);
    DonationResponseDto create(DonationCreateDto donationCreateDto);
    DonationResponseDto update(int id, DonationUpdateDto donationUpdateDto);
    void delete(int id);
    Page<DonationResponseDto> findDonationsByPage(int page, int size);
    List<DonationResponseDto> findByUserId(int userId);
    Map<String, BigDecimal> getTotalDonationsByDay();
    List<Map<String, Object>> getTopDonors(int limit);

    // Thêm phương thức getTotalDonationsByProgram vào đây
    Map<Integer, BigDecimal> getTotalDonationsByProgram();
}
