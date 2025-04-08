package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Donations.DonationCreateDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationUpdateDto;
import com.example.springdonateweb.Models.Entities.DonationsEntity;
import com.example.springdonateweb.Repositories.DonationsRepository;
import com.example.springdonateweb.Services.interfaces.IDonationsService;
import com.example.springdonateweb.Services.mappers.DonationsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationsService implements IDonationsService {

    private final DonationsRepository donationsRepository;
    private final DonationsMapper donationsMapper;
    public Map<Integer, BigDecimal> getTotalDonationsByProgram() {
        // Lấy tất cả các khoản quyên góp
        List<DonationResponseDto> donations = donationsRepository.findAll().stream()
                .map(donationEntity -> new DonationResponseDto(
                        donationEntity.getDonationId(),
                        donationEntity.getUserId(),
                        donationEntity.getProgramId(),
                        donationEntity.getAmount(),
                        donationEntity.getDonationDate(),
                        donationEntity.getDonorName()
                ))
                .collect(Collectors.toList());

        // Nhóm theo programId và tính tổng số tiền quyên góp cho mỗi chương trình
        Map<Integer, BigDecimal> programTotalDonations = donations.stream()
                .collect(Collectors.groupingBy(
                        DonationResponseDto::getProgramId,
                        Collectors.reducing(BigDecimal.ZERO, DonationResponseDto::getAmount, BigDecimal::add)
                ));

        System.out.println("Total Donations by Program: " + programTotalDonations); // Log để kiểm tra dữ liệu
        System.out.println("Donations Data: " + donations);
        System.out.println("Total Donations by Program: " + programTotalDonations);
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
                        Collectors.reducing(BigDecimal.ZERO, DonationsEntity::getAmount, BigDecimal::add)
                ));
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
                    DonationsEntity updatedDonation = donationsMapper.partialUpdate(donationUpdateDto, existingDonation);
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
}
