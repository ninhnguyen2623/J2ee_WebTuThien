package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Users.UserAddDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UsersController {
    private final IUsersService usersService;
    
    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) Boolean status) {
        
        // Check if any filters are active
        boolean isSearching = (searchTerm != null && !searchTerm.isEmpty()) || roleId != null || status != null;
        
        // Get users with filters if provided
        Page<UsersResponseDto> userPage;
        
        if (isSearching) {
            userPage = usersService.searchUsers(searchTerm, roleId, status, page, size);
        } else {
            userPage = usersService.findUsersByPage(page, size);
        }
        
        // Add attributes to model
        model.addAttribute("list", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalElements", userPage.getTotalElements());
        
        // Add filter values to model
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("roleId", roleId);
        model.addAttribute("status", status);
        model.addAttribute("isSearching", isSearching);
        
        return "admin/Users/index";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        UserAddDto newUser = new UserAddDto();
        // Set default role for testing
        newUser.setRoleId(2); // Default to User role
        model.addAttribute("user", newUser);
        // Add debug info
        model.addAttribute("debug", "User object initialized with default roleId=2");
        return "admin/Users/create";
    }
    
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") UserAddDto userAddDto, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/Users/create";
        }
        
        if (usersService.existsByEmail(userAddDto.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/admin/Users/create";
        }
        
        usersService.create(userAddDto);
        redirectAttributes.addFlashAttribute("success", "User created successfully");
        return "redirect:/admin/users";
    }
    
    // Trang chỉnh sửa người dùng
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        UsersResponseDto user = usersService.findByIdAndStatusTrue(id);
        if (user == null) {
            return "redirect:/admin/users"; // Quay lại danh sách nếu người dùng không tồn tại
        }
        model.addAttribute("user", user);
        return "admin/Users/edit"; // Trả về đường dẫn 'user/edit.html'
    }
    
    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable int id,
            @Valid @ModelAttribute("user") UserAddDto userAddDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/Users/edit"; // Hiển thị lại form chỉnh sửa nếu có lỗi
        }
        
        userAddDto.setId(id);
        usersService.update(userAddDto);
        redirectAttributes.addFlashAttribute("success", "User updated successfully");
        return "redirect:/admin/users"; // Quay lại danh sách người dùng
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes) {
        UsersResponseDto deletedUser = usersService.delete(id);
        if (deletedUser == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            
        } else {
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        }
        return "redirect:/admin/users";
    }
    
}
