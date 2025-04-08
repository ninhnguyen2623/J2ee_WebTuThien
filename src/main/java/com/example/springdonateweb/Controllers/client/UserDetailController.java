package com.example.springdonateweb.Controllers.client;

import com.example.springdonateweb.Models.Dtos.Users.ChangeEmailRequestDto;
import com.example.springdonateweb.Models.Dtos.Users.UserUpdateDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Services.UsersService;
import com.example.springdonateweb.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class UserDetailController {

    private final UsersService usersService;


    @GetMapping("/user-detail")
    public String userDetail(Model model) {
        String getUser = SecurityUtil.getSessionUser();
        UsersResponseDto user = usersService.findByEmail(getUser);
        model.addAttribute("user", user);
        return "client/user-detail";
    }

    @PostMapping("user-detail")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdateDto user,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "client/user-detail";
        }
        usersService.update(user);
        redirectAttributes.addFlashAttribute("success", "Update user successfully");
        return "redirect:/user-detail";

    }

    @GetMapping("/change-email")
    public String changeEmail(Model model) {

        return "client/change-email";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {

        return "client/change-password";
    }

    @PostMapping("/send-otp")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestParam("newEmail") String email) {
        Map<String, Object> response = new HashMap<>();
        log.info("Received email: " + email);
        if (usersService.existsByEmail(email)) {
            response.put("success", false);
            response.put("message", "Email đã được sử dụng");
            return ResponseEntity.badRequest().body(response);
        }
        String userId = SecurityUtil.getSessionUser();
        if (userId != null) {
            UsersResponseDto user = usersService.findByEmail(userId);
            int id = user.getId();
            usersService.sendOtp(id, email);
            response.put("success", true);
            response.put("message", "Success");
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("message", "Không tìm thấy người dùng");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/change-email")
    public String changeEmail(
            @ModelAttribute("changeEmail") ChangeEmailRequestDto requestDto,
            RedirectAttributes redirectAttributes
    ) {
        String userId = SecurityUtil.getSessionUser();
        if (userId != null) {
            UsersResponseDto user = usersService.findByEmail(userId);
            int id = user.getId();
            if (usersService.checkOtp(id, requestDto.getOtp())) {
                usersService.changeEmail(id);
                redirectAttributes.addAttribute("updateMailSuccess", true);
                return "redirect:/user-detail";
            } else {
                redirectAttributes.addAttribute("invalidOtpOrPass", true);
                return "redirect:/change-email";
            }
        }
        redirectAttributes.addAttribute("failUpdateEmail", true);
        return "redirect:/user-detail";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        String userId = SecurityUtil.getSessionUser();
        if (userId != null) {
            UsersResponseDto user = usersService.findByEmail(userId);
            int id = user.getId();
            if (usersService.checkPassword(id, oldPassword)) {
                    usersService.changePassword(id, newPassword);
                    redirectAttributes.addAttribute("updatePassSuccess", true);
                    return "redirect:/user-detail";
                
            } else {
                redirectAttributes.addAttribute("invalidOtpOrPass", true);
                return "redirect:/change-password";
            }
        }
        redirectAttributes.addAttribute("failUpdatePass", true);
        return "redirect:/user-password";
    }

}