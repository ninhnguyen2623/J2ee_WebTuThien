package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.Blogs.BlogCreateDto;
import com.example.springdonateweb.Models.Dtos.Blogs.BlogResponseDto;
import com.example.springdonateweb.Models.Dtos.Blogs.BlogUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IBlogService {
    Page<BlogResponseDto> findBlogsByPage(int page, int size);  // Add this method signature
    BlogResponseDto findById(int id);
    BlogResponseDto create(BlogCreateDto blogCreateDto);
    BlogResponseDto update(BlogUpdateDto blogUpdateDto);
    String uploadImage(MultipartFile image) throws IOException;
    void delete(int id);
    List<BlogResponseDto> findAll();
}
