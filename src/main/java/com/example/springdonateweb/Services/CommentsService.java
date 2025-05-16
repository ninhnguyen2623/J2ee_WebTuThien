package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Comments.CommentCreateDto;
import com.example.springdonateweb.Models.Dtos.Comments.CommentResponseDto;
import com.example.springdonateweb.Models.Dtos.Comments.CommentUpdateDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Models.Entities.CommentsEntity;
import com.example.springdonateweb.Repositories.CommentsRepository;
import com.example.springdonateweb.Services.interfaces.ICommentsService;
import com.example.springdonateweb.Services.interfaces.IProgramsService;
import com.example.springdonateweb.Services.interfaces.IUsersService;
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
    private final IUsersService usersService;
    private final IProgramsService programsService;
    
    @Override
    public List<CommentResponseDto> findAll() {
        return commentsRepository.findAll().stream()
                                 .map(this::enrichCommentWithNames)
                                 .collect(Collectors.toList());
    }
    
    @Override
    public CommentResponseDto findById(int id) {
        return commentsRepository.findById(id)
                                 .map(this::enrichCommentWithNames)
                                 .orElse(null);
    }
    
    @Override
    public Page<CommentResponseDto> findCommentsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentsEntity> commentsPage = commentsRepository.findAll(pageable);
        return commentsPage.map(this::enrichCommentWithNames);
    }
    
    @Override
    public Page<CommentResponseDto> findCommentsByPage(Pageable pageable) {
        Page<CommentsEntity> commentsPage = commentsRepository.findAll(pageable);
        return commentsPage.map(this::enrichCommentWithNames);
    }
    
    @Override
    public CommentResponseDto create(CommentCreateDto commentCreateDto) {
        CommentsEntity commentsEntity = commentsMapper.toEntity(commentCreateDto);
        CommentsEntity savedComment = commentsRepository.save(commentsEntity);
        return enrichCommentWithNames(savedComment);
    }
    
    @Override
    public CommentResponseDto update(int id, CommentUpdateDto commentUpdateDto) {
        return commentsRepository.findById(id)
                                 .map(comment -> {
                                     CommentsEntity updatedComment = commentsMapper.partialUpdate(commentUpdateDto, comment);
                                     return enrichCommentWithNames(commentsRepository.save(updatedComment));
                                 })
                                 .orElse(null);
    }
    
    @Override
    public void delete(int id) {
        commentsRepository.deleteById(id);
    }
    
    // Helper method to enrich comments with user and program names
    private CommentResponseDto enrichCommentWithNames(CommentsEntity commentEntity) {
        CommentResponseDto dto = commentsMapper.toDto(commentEntity);
        
        // Get user name
        if (dto.getUserId() != null) {
            UsersResponseDto user = usersService.findById(dto.getUserId());
            if (user != null) {
                dto.setUserName(user.getName());
            } else {
                dto.setUserName("Unknown User");
            }
        } else {
            dto.setUserName("Anonymous");
        }
        
        // Get program name
        if (dto.getProgramId() != null) {
            ProgramsResponseDto program = programsService.findById(dto.getProgramId());
            if (program != null) {
                dto.setProgramName(program.getName());
            } else {
                dto.setProgramName("Unknown Program");
            }
        } else {
            dto.setProgramName("N/A");
        }
        
        return dto;
    }
}
