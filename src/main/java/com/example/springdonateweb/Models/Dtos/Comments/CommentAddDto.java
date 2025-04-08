package com.example.springdonateweb.Models.Dtos.Comments;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentAddDto {
    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Program ID is required")
    private Integer programId;

    @NotBlank(message = "Content is required")
    private String content;

    private Timestamp createdAt;
}
