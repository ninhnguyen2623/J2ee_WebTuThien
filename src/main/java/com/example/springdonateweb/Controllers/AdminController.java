package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Services.DonationsService;
import com.example.springdonateweb.Services.ProgramsService;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller class for handling admin dashboard related requests
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    
    private final ProgramsService programsService;
    private final DonationsService donationsService;
    private final IUsersService usersService;
    
    /**
     * Display the admin dashboard/index page
     *
     * @param model Model object to pass data Ato the view
     * @return the admin index view
     */
    @GetMapping
    public String adminIndex(Model model) {
        // Get all available programs
        List<ProgramsResponseDto> programs = programsService.findAll();
        
        // Count active programs
        long activePrograms = programs.stream()
                                      .filter(ProgramsResponseDto::isStatus)
                                      .count();
        
        // Calculate completion rate as percentage of active programs
        double completionRate = programs.isEmpty() ? 0 : (activePrograms * 100.0 / programs.size());
        
        // Add data to the model
        model.addAttribute("programCount", programs.size());
        model.addAttribute("completionRate", Math.round(completionRate));
        model.addAttribute("recentPrograms", programs.size() > 5 ? programs.subList(0, 5) : programs);
        model.addAttribute("donationCount", donationsService.findAll().size());
        model.addAttribute("userCount", usersService.findAll().size());
        
        return "admin/index";
    }
}