package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.Programs.ProgramCreateDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProgramsService {
    List<ProgramsResponseDto> findAll();
    ProgramsResponseDto findById(int id);
    ProgramsResponseDto create(ProgramCreateDto programCreateDto);
    ProgramsResponseDto update(ProgramUpdateDto programUpdateDto);
    void delete(int id);
    List<ProgramsResponseDto> findByStatusTrue();
    // Phương thức mới cho phân trang

    Page<ProgramsResponseDto> findProgramsByPage(int page, int size);
    List<ProgramsResponseDto> findByCategory_CategoryId(int categoryId);

    Page<ProgramsResponseDto> findProgramsByPageAndStatusTrue(int page, int size);
    ProgramsResponseDto findByProgramIdAndStatusTrue(int id);
}
