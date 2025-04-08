package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Blogs.BlogResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Models.Entities.DonationsEntity;
import com.example.springdonateweb.Services.BlogService;
import com.example.springdonateweb.Services.CategoriesService;
import com.example.springdonateweb.Services.DonationsService;
import com.example.springdonateweb.Services.ProgramsService;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import com.example.springdonateweb.util.AmountFormatter;
import com.example.springdonateweb.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.nio.file.OpenOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class HomeController {

    private final ProgramsService programsService;
    private final CategoriesService categoriesService;
    private final DonationsService donationService;
    private final BlogService blogService;


    IUsersService usersService;

    @GetMapping("/")
    public String index(Model model, @RequestParam(value = "filter", required = false) String filter) {
        String userId = SecurityUtil.getSessionUser(); 


        if (userId != null) {
            UsersResponseDto user = usersService.findByEmail(userId); 
            List<ProgramsResponseDto> program = programsService.findByStatusTrue();
            if (user != null) {
               
                if (user.getRoleId() == 1) {  
                    return "redirect:/admin/user"; 
                }

                model.addAttribute("program", program);
                model.addAttribute("userEmail", user.getEmail());

                return "index"; 
            } else {
                return "redirect:/login"; // Redirect to login if user not found
            }
        } else {
            return "index"; 
        }
    }
    

   @GetMapping("/my-donations")
    public String getMyDonations(Model model, Principal principal) {
        String getUser = SecurityUtil.getSessionUser();
        UsersResponseDto user = usersService.findByEmail(getUser);
        List<DonationResponseDto> donations = donationService.findByUserId(user.getId());
        model.addAttribute("donations", donations);
        return "client/my-donation";
    }
    


}
