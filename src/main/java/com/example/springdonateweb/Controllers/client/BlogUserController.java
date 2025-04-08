package com.example.springdonateweb.Controllers.client;


import com.example.springdonateweb.Models.Dtos.Blogs.BlogResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Models.Dtos.Users.ChangeEmailRequestDto;
import com.example.springdonateweb.Models.Dtos.Users.UserUpdateDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Models.Entities.DonationsEntity;
import com.example.springdonateweb.Services.BlogService;
import com.example.springdonateweb.Services.CategoriesService;
import com.example.springdonateweb.Services.DonationsService;
import com.example.springdonateweb.Services.ProgramsService;
import com.example.springdonateweb.Services.UsersService;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import com.example.springdonateweb.util.AmountFormatter;
import com.example.springdonateweb.util.SecurityUtil;

import groovy.lang.Binding;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.OpenOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class BlogUserController {
    private final ProgramsService programsService;
    private final CategoriesService categoriesService;
    private final DonationsService donationService;
    private final BlogService blogService;

    @GetMapping("/blog")
    public String getAllBlog(Model model) {
        List<BlogResponseDto> blog = blogService.findAll();
        List<ProgramsResponseDto> program = programsService.findByStatusTrue();
        model.addAttribute("program", program);
        model.addAttribute("blog", blog);
        return "client/blog"; 
    }
    @GetMapping("/blog/{id}")
    public String getBlogDetail(Model model,@PathVariable int id) {
        BlogResponseDto blog = blogService.findById(id);
        List<ProgramsResponseDto> program = programsService.findByStatusTrue();
        model.addAttribute("program", program);
        model.addAttribute("blog", blog);
        return "client/blog-detail"; 
    }
}
