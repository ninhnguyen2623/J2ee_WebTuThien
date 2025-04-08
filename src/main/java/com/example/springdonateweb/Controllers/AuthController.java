package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Users.UserCreateDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Models.Entities.UsersEntity;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import com.example.springdonateweb.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@ComponentScan("com.example.springdonateweb")
public class AuthController {


    IUsersService usersService;
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerPage(@ModelAttribute("register") UserCreateDto register) {
        String user = SecurityUtil.getSessionUser();
        if (user != null) {
            return "redirect:/login"; 
        } else {
            return "auth/register"; 
        }
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("register") UserCreateDto register,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            return "auth/register"; 
        }
        if(usersService.existsByEmail(register.getEmail())){
            redirectAttributes.addFlashAttribute("existEmail", "true");
            redirectAttributes.addFlashAttribute("register", register);
            return "redirect:/register";
        }



        usersService.register(register);
        redirectAttributes.addFlashAttribute("success", "true");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(RedirectAttributes redirectAttributes) {
    String user = SecurityUtil.getSessionUser();
    if (user != null) {
        redirectAttributes.addFlashAttribute("loginSuccess", "Login successful!");
        return "redirect:/";
    }
    return "auth/login"; 
}

    @PostMapping("/forgot-password")
    public String fogotPassword(
            @RequestParam("forgot-email") String email,
            RedirectAttributes redirectAttributes
    ) {
        if(!usersService.existsByEmail(email)){
            redirectAttributes.addAttribute("userNotFound", "true");
            return "redirect:/login";

        }
        usersService.resetPassword(email);
        redirectAttributes.addAttribute("resetPassword", "true");
        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(
            @ModelAttribute("forgot") UserCreateDto forgot

    ){
        String user = SecurityUtil.getSessionUser();
        if (user != null) {
            return "redirect:/"; 
        }
        else {
            return "auth/forgetPassword"; 
        }
        
    }

}
