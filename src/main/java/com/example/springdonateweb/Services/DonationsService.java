package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Donations.DonationCreateDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationUpdateDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Models.Entities.DonationsEntity;
import com.example.springdonateweb.Repositories.DonationsRepository;
import com.example.springdonateweb.Services.interfaces.IDonationsService;
import com.example.springdonateweb.Services.interfaces.IProgramsService;
import com.example.springdonateweb.Services.mappers.DonationsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationsService implements IDonationsService {
    
    private final DonationsRepository donationsRepository;
    private final DonationsMapper donationsMapper;
    private final IProgramsService programsService;
    
    public Map<Integer, BigDecimal> getTotalDonationsByProgram() {
        // Lấy tất cả các khoản quyên góp
        List<DonationResponseDto> donations = donationsRepository.findAll().stream()
                                                                 .map(donationEntity -> new DonationResponseDto(
                                                                         donationEntity.getDonationId(),
                                                                         donationEntity.getUserId(),
                                                                         donationEntity.getProgramId(),
                                                                         donationEntity.getAmount(),
                                                                         donationEntity.getDonationDate(),
                                                                         donationEntity.getDonorName()))
                                                                 .collect(Collectors.toList());
        
        // Nhóm theo programId và tính tổng số tiền quyên góp cho mỗi chương trình
        Map<Integer, BigDecimal> programTotalDonations = donations.stream()
                                                                  .collect(Collectors.groupingBy(
                                                                          DonationResponseDto::getProgramId,
                                                                          Collectors.reducing(BigDecimal.ZERO, DonationResponseDto::getAmount, BigDecimal::add)));
        return programTotalDonations;
    }
    
    @Override
    public List<Map<String, Object>> getTopDonors(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return donationsRepository.findTopDonors(pageable);
    }
    
    @Override
    public Map<String, BigDecimal> getTotalDonationsByDay() {
        return donationsRepository.findAll().stream()
                                  .collect(Collectors.groupingBy(
                                          donation -> donation.getDonationDate().toLocalDate().toString(), // Group by date
                                          Collectors.reducing(BigDecimal.ZERO, DonationsEntity::getAmount, BigDecimal::add)));
    }
    
    @Override
    public List<DonationResponseDto> findAll() {
        return donationsRepository.findAll().stream()
                                  .map(donationsMapper::toDto)
                                  .collect(Collectors.toList());
    }
    
    @Override
    public Page<DonationResponseDto> findDonationsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationsEntity> donationPage = donationsRepository.findAll(pageable);
        return donationPage.map(donationsMapper::toDto);
    }
    
    @Override
    public Page<DonationResponseDto> findDonationsByPage(Pageable pageable) {
        Page<DonationsEntity> donationPage = donationsRepository.findAll(pageable);
        return donationPage.map(donationsMapper::toDto);
    }
    
    @Override
    public DonationResponseDto findById(int id) {
        return donationsRepository.findById(id)
                                  .map(donationsMapper::toDto)
                                  .orElse(null);
    }
    
    @Override
    public DonationResponseDto create(DonationCreateDto donationCreateDto) {
        DonationsEntity donationsEntity = donationsMapper.toEntity(donationCreateDto);
        DonationsEntity savedDonation = donationsRepository.save(donationsEntity);
        return donationsMapper.toDto(savedDonation);
    }
    
    @Override
    public DonationResponseDto update(int id, DonationUpdateDto donationUpdateDto) {
        return donationsRepository.findById(id)
                                  .map(existingDonation -> {
                                      DonationsEntity updatedDonation = donationsMapper.partialUpdate(donationUpdateDto,
                                                                                                      existingDonation);
                                      return donationsMapper.toDto(donationsRepository.save(updatedDonation));
                                  })
                                  .orElse(null);
    }
    
    @Override
    public void delete(int id) {
        donationsRepository.deleteById(id);
    }
    
    @Override
    public List<DonationResponseDto> findByUserId(int userId) {
        return donationsRepository.findByUserId(userId).stream()
                                  .map(donationsMapper::toDto)
                                  .collect(Collectors.toList());
    }
    
    @Override
    public Page<DonationResponseDto> searchDonations(String searchTerm, LocalDate fromDate, LocalDate toDate, int page,
                                                     int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        // Chuyển đổi LocalDate sang LocalDateTime nếu cần
        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;
        
        if (fromDate != null) {
            fromDateTime = fromDate.atStartOfDay();
        }
        
        if (toDate != null) {
            toDateTime = toDate.atTime(23, 59, 59);
        }
        
        // Tìm kiếm với các tiêu chí
        Page<DonationsEntity> donations;
        
        if (searchTerm != null && !searchTerm.isEmpty()) {
            if (fromDateTime != null && toDateTime != null) {
                // Tìm kiếm theo cả tên và khoảng thời gian
                donations = donationsRepository.findByDonorNameContainingIgnoreCaseAndDonationDateBetween(
                        searchTerm, fromDateTime, toDateTime, pageable);
            } else if (fromDateTime != null) {
                // Tìm kiếm theo tên và từ ngày cụ thể
                donations = donationsRepository.findByDonorNameContainingIgnoreCaseAndDonationDateAfter(
                        searchTerm, fromDateTime, pageable);
            } else if (toDateTime != null) {
                // Tìm kiếm theo tên và đến ngày cụ thể
                donations = donationsRepository.findByDonorNameContainingIgnoreCaseAndDonationDateBefore(
                        searchTerm, toDateTime, pageable);
            } else {
                // Chỉ tìm kiếm theo tên
                donations = donationsRepository.findByDonorNameContainingIgnoreCase(searchTerm, pageable);
            }
        } else {
            if (fromDateTime != null && toDateTime != null) {
                // Chỉ tìm kiếm theo khoảng thời gian
                donations = donationsRepository.findByDonationDateBetween(fromDateTime, toDateTime, pageable);
            } else if (fromDateTime != null) {
                // Chỉ tìm kiếm từ ngày cụ thể
                donations = donationsRepository.findByDonationDateAfter(fromDateTime, pageable);
            } else if (toDateTime != null) {
                // Chỉ tìm kiếm đến ngày cụ thể
                donations = donationsRepository.findByDonationDateBefore(toDateTime, pageable);
            } else {
                // Không có tiêu chí tìm kiếm, lấy tất cả
                donations = donationsRepository.findAll(pageable);
            }
        }
        
        // Chuyển đổi sang DTO
        return donations.map(donationsMapper::toDto);
    }
    
    @Override
    public Map<String, Double> getTotalDonationByProgram() {
        try {
            // Get program names
            Map<Integer, String> programNames = programsService.findAll().stream()
                                                               .collect(Collectors.toMap(ProgramsResponseDto::getProgramId, ProgramsResponseDto::getName));
            
            // Get donation totals by program ID from repository
            List<Object[]> results = donationsRepository.sumDonationsByProgram();
            
            // Create a map of program names to donation amounts
            Map<String, Double> donationsByProgram = new LinkedHashMap<>();
            
            // Process results
            for (Object[] result : results) {
                Integer programId = (Integer) result[0];
                BigDecimal amount = (BigDecimal) result[1];
                
                String programName = programNames.getOrDefault(programId, "Chương trình #" + programId);
                donationsByProgram.put(programName, amount.doubleValue());
            }
            
            return donationsByProgram;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error getting donation by program: " + e.getMessage());
            e.printStackTrace();
            
            // Return empty map in case of error
            return new HashMap<>();
        }
    }
    
    @Override
    public Map<String, Integer> getDonationCountsByMonth(int months) {
        try {
            // Get current date and calculate start date
            LocalDate now = LocalDate.now();
            LocalDate startDate = now.minusMonths(months - 1).withDayOfMonth(1);
            
            // Create a map to store donation counts by month
            Map<String, Integer> donationCounts = new LinkedHashMap<>();
            
            // Generate month labels for the last X months
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
            for (int i = 0; i < months; i++) {
                LocalDate date = startDate.plusMonths(i);
                String monthYear = date.format(monthFormatter);
                donationCounts.put("Tháng " + monthYear, 0);
            }
            
            // Query donations data for the specified period
            List<Object[]> results = donationsRepository.countDonationsByMonth(
                    startDate.atStartOfDay(),
                    now.plusDays(1).atStartOfDay());
            
            // Process results
            for (Object[] result : results) {
                Integer year = (Integer) result[0];
                Integer month = (Integer) result[1];
                Long count = (Long) result[2];
                
                String monthStr = String.format("%02d", month);
                String key = "Tháng " + monthStr + "/" + year;
                
                if (donationCounts.containsKey(key)) {
                    donationCounts.put(key, count.intValue());
                }
            }
            
            return donationCounts;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error getting donations by month: " + e.getMessage());
            e.printStackTrace();
            
            // Return empty map in case of error
            return new LinkedHashMap<>();
        }
    }
}
