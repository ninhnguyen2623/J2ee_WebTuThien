package com.example.springdonateweb.Models.Dtos.Blogs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogCreateDto {
    private String title;
    private String content;
    private MultipartFile imageUrl;  // Chứa URL hình ảnh
    private boolean status;

}