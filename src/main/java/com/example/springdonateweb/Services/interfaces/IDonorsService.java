package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.Donors.DonorCreateDto;
import com.example.springdonateweb.Models.Dtos.Donors.DonorResponseDto;
import com.example.springdonateweb.Models.Dtos.Donors.DonorUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDonorsService {
    List<DonorResponseDto> findAll();
    DonorResponseDto findById(int id);
    DonorResponseDto create(DonorCreateDto donorCreateDto);
    DonorResponseDto update(int id, DonorUpdateDto donorUpdateDto);
    void delete(int id);
    Page<DonorResponseDto> findDonorsByPage(int page, int size);
}
