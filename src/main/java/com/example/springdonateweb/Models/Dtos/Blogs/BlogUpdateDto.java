package com.example.springdonateweb.Models.Dtos.Blogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogUpdateDto {
    private int id;
    private String title;
    private String content;
    private String imageUrl;
    private boolean status;
}