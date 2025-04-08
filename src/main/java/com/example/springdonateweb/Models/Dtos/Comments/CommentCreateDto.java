package com.example.springdonateweb.Models.Dtos.Comments;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentCreateDto {
    private Integer userId;
    private Integer programId;
    private String content;
    private LocalDateTime createdAt;
}
