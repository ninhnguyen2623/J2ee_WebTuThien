package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Comments.CommentCreateDto;
import com.example.springdonateweb.Models.Dtos.Comments.CommentResponseDto;
import com.example.springdonateweb.Models.Dtos.Comments.CommentUpdateDto;
import com.example.springdonateweb.Models.Entities.CommentsEntity;
import com.example.springdonateweb.Repositories.CommentsRepository;
import com.example.springdonateweb.Services.interfaces.ICommentsService;
import com.example.springdonateweb.Services.mappers.CommentsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService implements ICommentsService {

    private final CommentsRepository commentsRepository;
    private final CommentsMapper commentsMapper;

    @Override
    public List<CommentResponseDto> findAll() {
        return commentsRepository.findAll().stream()
                .map(commentsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto findById(int id) {
        return commentsRepository.findById(id)
                .map(commentsMapper::toDto)
                .orElse(null);
    }
    public Page<CommentResponseDto> findCommentsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentsEntity> commentPage = commentsRepository.findAll(pageable);
        return commentPage.map(commentsMapper::toDto);
    }
    @Override
    public CommentResponseDto create(CommentCreateDto commentCreateDto) {
        CommentsEntity commentsEntity = commentsMapper.toEntity(commentCreateDto);
        CommentsEntity savedComment = commentsRepository.save(commentsEntity);
        return commentsMapper.toDto(savedComment);
    }
    @Override
    public CommentResponseDto update(int id, CommentUpdateDto commentUpdateDto) {
        return commentsRepository.findById(id)
                .map(existingComment -> {
                    // Thực hiện cập nhật mà không thay đổi commentId
                    if (commentUpdateDto.getUserId() != null) {
                        existingComment.setUserId(commentUpdateDto.getUserId());
                    }
                    if (commentUpdateDto.getProgramId() != null) {
                        existingComment.setProgramId(commentUpdateDto.getProgramId());
                    }
                    if (commentUpdateDto.getContent() != null) {
                        existingComment.setContent(commentUpdateDto.getContent());
                    }
                    return commentsMapper.toDto(commentsRepository.save(existingComment));
                })
                .orElse(null);
    }


    @Override
    public void delete(int id) {
        commentsRepository.deleteById(id);
    }
}
