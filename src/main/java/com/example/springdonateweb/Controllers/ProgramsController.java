package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Programs.ProgramCreateDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramUpdateDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Services.interfaces.ICategoriesService;
import com.example.springdonateweb.Services.interfaces.IProgramsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/programs")
public class ProgramsController {
    
    private final IProgramsService programsService;
    private final ICategoriesService categoriesService;
    
    private static final String UPLOAD_DIR = "src/main/resources/static/img/program/";
    
    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Boolean status) {
        
        // Search functionality
        Page<ProgramsResponseDto> programPage;
        boolean isSearching = (search != null && !search.isEmpty()) || categoryId != null || status != null;
        
        if (isSearching) {
            programPage = programsService.searchPrograms(search, categoryId, status, page, size);
        } else {
            programPage = programsService.findProgramsByPageAndStatusTrue(page, size);
        }
        
        model.addAttribute("list", programPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", programPage.getTotalPages());
        model.addAttribute("isSearching", isSearching);
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedStatus", status);
        
        int start = page * size + 1;
        int end = start + programPage.getNumberOfElements() - 1;
        
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("totalElements", programPage.getTotalElements());
        
        // Add categories for the filter dropdown
        model.addAttribute("categories", categoriesService.findAll2());
        
        return "admin/Programs/index";
    }
    
    // Trang thêm chương trình mới
    @GetMapping("/create")
    public String createProgramForm(Model model) {
        model.addAttribute("program", new ProgramCreateDto());
        model.addAttribute("categories", categoriesService.findAll2()); // Sử dụng findAll2 thông qua dependency
        // injection
        return "admin/Programs/create"; // Đường dẫn tới create.html trong thư mục Programs
    }
    
    @PostMapping("/create")
    public String createProgram(
            @Valid @ModelAttribute("program") ProgramCreateDto programCreateDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        // Kiểm tra các lỗi validation
        if (result.hasErrors()) {
            // Nếu có lỗi, trả về trang tạo chương trình với lỗi hiển thị
            return "admin/Programs/create";
        }
        
        // Xử lý upload ảnh
        MultipartFile file = programCreateDto.getImage();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.write(path, bytes);
                programCreateDto.setImage(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // Lưu chương trình vào cơ sở dữ liệu
        programsService.create(programCreateDto);
        redirectAttributes.addFlashAttribute("success", "Program created successfully");
        return "redirect:/admin/programs";
    }
    
    // Trang chỉnh sửa chương trình
    @GetMapping("/edit/{id}")
    public String editProgramForm(@PathVariable int id, Model model) {
        try {
            ProgramsResponseDto program = programsService.findById(id);
            if (program == null) {
                model.addAttribute("error", "Không tìm thấy chương trình với ID: " + id);
                model.addAttribute("debug", "Không tìm thấy chương trình với ID: " + id);
                return "admin/Programs/edit"; // Hiển thị trang edit với thông báo lỗi
            }
            model.addAttribute("program", program);
            model.addAttribute("categories", categoriesService.findAll2()); // Thêm danh sách categories vào model
            model.addAttribute("debug",
                               "ID: " + id + ", Program: " + program.getName() + ", CategoryId: " + program.getCategoryId());
            return "admin/Programs/edit"; // Đường dẫn tới edit.html trong thư mục Programs
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi khi tải chương trình: " + e.getMessage());
            model.addAttribute("debug", "Exception khi tải ID: " + id + " - " + e.toString());
            return "admin/Programs/edit";
        }
    }
    
    @PostMapping("/edit/{id}")
    public String editProgram(
            @PathVariable int id,
            @Valid @ModelAttribute("program") ProgramUpdateDto programUpdateDto,
            BindingResult result,
            @RequestParam(value = "image", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/Programs/edit";
        }
        
        // Xử lý upload ảnh nếu có
        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, bytes);
                programUpdateDto.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        programUpdateDto.setProgramId(id); // Đảm bảo ID được cập nhật đúng
        programsService.update(programUpdateDto);
        redirectAttributes.addFlashAttribute("success", "Program updated successfully");
        return "redirect:/admin/programs";
    }
    
    // Xóa chương trình
    @GetMapping("/delete/{id}")
    public String deleteProgram(@PathVariable int id, RedirectAttributes redirectAttributes) {
        programsService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Program deleted successfully");
        return "redirect:/admin/programs";
    }
}
