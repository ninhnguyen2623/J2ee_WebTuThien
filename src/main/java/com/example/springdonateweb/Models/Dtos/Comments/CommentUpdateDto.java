package com.example.springdonateweb.Models.Dtos.Comments;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {
    private int commentId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Program ID is required")
    private Integer programId;

    @NotBlank(message = "Content is required")
    private String content;


}
