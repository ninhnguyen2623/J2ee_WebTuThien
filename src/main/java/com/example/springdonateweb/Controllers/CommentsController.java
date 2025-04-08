package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Comments.CommentCreateDto;
import com.example.springdonateweb.Models.Dtos.Comments.CommentResponseDto;
import com.example.springdonateweb.Models.Dtos.Comments.CommentUpdateDto;
import com.example.springdonateweb.Services.interfaces.ICommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class CommentsController {

    private final ICommentsService commentsService;

    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CommentResponseDto> commentPage = commentsService.findCommentsByPage(page, size);
        model.addAttribute("comments", commentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commentPage.getTotalPages());
        return "admin/Comments/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("comment", new CommentCreateDto());
        return "admin/Comments/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CommentCreateDto commentCreateDto) {
        commentsService.create(commentCreateDto);
        return "redirect:/admin/comments";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        CommentResponseDto comment = commentsService.findById(id);
        if (comment == null) return "redirect:/admin/comments";
        model.addAttribute("comment", comment);
        return "admin/Comments/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute CommentUpdateDto commentUpdateDto) {
        commentsService.update(id, commentUpdateDto);
        return "redirect:/admin/comments";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        commentsService.delete(id);
        return "redirect:/admin/comments";
    }
}
