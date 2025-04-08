package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Users.UserAddDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import com.example.springdonateweb.util.SecurityUtil;
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
@RequestMapping("/admin/user")
public class UsersController {
    private final IUsersService usersService;

    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UsersResponseDto> userPage = usersService.findUsersByPage(page, size);
        model.addAttribute("list", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "user/index"; // Đường dẫn tới view index.html
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new UserAddDto());
        return "user/create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") UserAddDto userAddDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/create";
        }

        if (usersService.existsByEmail(userAddDto.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/admin/user/create";
        }

        usersService.create(userAddDto);
        redirectAttributes.addFlashAttribute("success", "User created successfully");
        return "redirect:/admin/user";
    }

    // Trang chỉnh sửa người dùng
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        UsersResponseDto user = usersService.findById(id);
        if (user == null) {
            return "redirect:/admin/user"; // Quay lại danh sách nếu người dùng không tồn tại
        }
        model.addAttribute("user", user);
        return "user/edit"; // Trả về đường dẫn 'user/edit.html'
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable int id,
            @Valid @ModelAttribute("user") UserAddDto userAddDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/edit"; // Hiển thị lại form chỉnh sửa nếu có lỗi
        }

        userAddDto.setId(id);
        usersService.update(userAddDto);
        redirectAttributes.addFlashAttribute("success", "User updated successfully");
        return "redirect:/admin/user"; // Quay lại danh sách người dùng
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes) {
        UsersResponseDto deletedUser = usersService.delete(id);
        if (deletedUser == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");

        } else {
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        }
        return "redirect:/admin/user";
    }

}
