package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Blogs.BlogCreateDto;
import com.example.springdonateweb.Models.Dtos.Blogs.BlogResponseDto;
import com.example.springdonateweb.Models.Dtos.Blogs.BlogUpdateDto;
import com.example.springdonateweb.Services.interfaces.IBlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/blogs")
public class BlogController {

    private final IBlogService blogService;

    private static final String UPLOAD_DIR = "src/main/resources/static/img/blog/";


    @GetMapping("")
    public String listBlogs(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Page<BlogResponseDto> blogPage = blogService.findBlogsByPage(page, size);
        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogPage.getTotalPages());
        return "admin/Blogs/index";  // Đường dẫn tới trang index.html
    }

    @GetMapping("/create")
    public String createBlogForm(Model model) {
        model.addAttribute("blog", new BlogCreateDto());
        return "admin/Blogs/create";  // Đường dẫn tới trang tạo blog
    }

    @PostMapping("/create")
    public String createBlog(@Valid @ModelAttribute("blog") BlogCreateDto blogCreateDto,
                             @RequestParam("imageUrl") MultipartFile imageUrl, // Xử lý tệp hình ảnh
                             BindingResult result, RedirectAttributes redirectAttributes) throws IOException {

        if (result.hasErrors()) {
            return "admin/Blogs/create";
        }

        MultipartFile file = blogCreateDto.getImageUrl();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.write(path, bytes);
                blogCreateDto.setImageUrl(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        blogService.create(blogCreateDto);
        redirectAttributes.addFlashAttribute("success", "Blog created successfully");
        return "redirect:/admin/blogs";
    }


    @GetMapping("/edit/{id}")
    public String editBlogForm(@PathVariable int id, Model model) {
        BlogResponseDto blog = blogService.findById(id);
        if (blog == null) {
            return "redirect:/admin/blogs";
        }
        model.addAttribute("blog", blog);
        return "admin/Blogs/edit";  // Đường dẫn tới trang sửa blog
    }

    @PostMapping("/edit/{id}")
    public String editBlog(@PathVariable int id, @Valid @ModelAttribute("blog") BlogUpdateDto blogUpdateDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/Blogs/edit";
        }
        blogUpdateDto.setId(id);  // Đảm bảo ID được cập nhật đúng
        blogService.update(blogUpdateDto);
        redirectAttributes.addFlashAttribute("success", "Blog updated successfully");
        return "redirect:/admin/blogs";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable int id, RedirectAttributes redirectAttributes) {
        blogService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Blog deleted successfully");
        return "redirect:/admin/blogs";
    }
}
