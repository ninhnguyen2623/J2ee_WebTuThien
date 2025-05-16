package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Donors.DonorCreateDto;
import com.example.springdonateweb.Models.Dtos.Donors.DonorResponseDto;
import com.example.springdonateweb.Models.Dtos.Donors.DonorUpdateDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Services.interfaces.IDonorsService;
import com.example.springdonateweb.Services.interfaces.IProgramsService;
import com.example.springdonateweb.Services.interfaces.IDonationsService;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/donors")
public class DonorsController {
    
    private final IDonorsService donorsService;
    private final IProgramsService programsService;
    private final IDonationsService donationsService;
    private final IUsersService usersService;
    
    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String searchField,
            @RequestParam(required = false) Integer programId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount) {
        
        // Get all donors with pagination
        Page<DonorResponseDto> donorsPage = donorsService.findDonorsByPage(page, size);
        List<DonorResponseDto> filteredDonors = new ArrayList<>(donorsPage.getContent());
        
        // Fetch program names for display
        List<ProgramsResponseDto> programs = programsService.findAll();
        Map<Integer, String> programNames = programs.stream()
                                                    .collect(Collectors.toMap(ProgramsResponseDto::getProgramId, ProgramsResponseDto::getName));
        
        // Fetch user names for display - modified to get all users regardless of status
        List<UsersResponseDto> users = usersService.findAll();
        Map<Integer, String> userNames = users.stream()
                                              .collect(Collectors.toMap(UsersResponseDto::getId, UsersResponseDto::getName));
        
        // Create a map of users by ID for efficient lookups
        Map<Integer, UsersResponseDto> usersMap = users.stream()
                                                       .collect(Collectors.toMap(UsersResponseDto::getId, user -> user));
        
        // Fetch donation amounts for display and filtering
        Map<Integer, BigDecimal> donationAmounts = new HashMap<>();
        Map<Integer, DonationResponseDto> donationsMap = new HashMap<>();
        List<DonorResponseDto> donorsToRemove = new ArrayList<>();
        
        for (DonorResponseDto donor : filteredDonors) {
            DonationResponseDto donation = donationsService.findById(donor.getDonationId());
            if (donation != null) {
                donationAmounts.put(donor.getDonationId(), donation.getAmount());
                donationsMap.put(donor.getDonationId(), donation);
                
                // Filter by amount range if specified
                boolean removeByMinAmount = minAmount != null && donation.getAmount().compareTo(minAmount) < 0;
                boolean removeByMaxAmount = maxAmount != null && donation.getAmount().compareTo(maxAmount) > 0;
                
                if (removeByMinAmount || removeByMaxAmount) {
                    donorsToRemove.add(donor);
                }
            }
        }
        
        // Apply filters
        filteredDonors.removeAll(donorsToRemove);
        
        // Filter by program if specified
        if (programId != null) {
            filteredDonors = filteredDonors.stream()
                                           .filter(donor -> donor.getProgramId().equals(programId))
                                           .collect(Collectors.toList());
        }
        
        // Filter by search term if specified
        if (searchTerm != null && !searchTerm.isEmpty()) {
            List<DonorResponseDto> searchResults = new ArrayList<>();
            
            // Convert search term to lowercase for case-insensitive matching
            String searchTermLower = searchTerm.toLowerCase();
            
            for (DonorResponseDto donor : filteredDonors) {
                boolean matchFound = false;
                
                // Search in all fields since searchField selection has been removed
                // Search in donor name
                DonationResponseDto donation = donationsMap.get(donor.getDonationId());
                if (donation != null && donation.getDonorName() != null &&
                        donation.getDonorName().toLowerCase().contains(searchTermLower)) {
                    matchFound = true;
                }
                
                // Search in user name
                else if (userNames.containsKey(donor.getUserId()) &&
                        userNames.get(donor.getUserId()).toLowerCase().contains(searchTermLower)) {
                    matchFound = true;
                }
                
                // Search in user details (email, phone)
                else {
                    UsersResponseDto user = usersMap.get(donor.getUserId());
                    if (user != null) {
                        // Check email
                        if (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchTermLower)) {
                            matchFound = true;
                        }
                        // Check phone number
                        else if (user.getPhoneNumber() != null &&
                                user.getPhoneNumber().toLowerCase().contains(searchTermLower)) {
                            matchFound = true;
                        }
                    }
                }
                
                if (matchFound) {
                    searchResults.add(donor);
                }
            }
            filteredDonors = searchResults;
        }
        
        // Add attributes to model
        model.addAttribute("donors", filteredDonors);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) filteredDonors.size() / size));
        model.addAttribute("totalItems", filteredDonors.size());
        model.addAttribute("programNames", programNames);
        model.addAttribute("userNames", userNames);
        model.addAttribute("donationAmounts", donationAmounts);
        model.addAttribute("programs", programs);
        model.addAttribute("searchTerm", searchTerm);
        // Keep this attribute for backward compatibility but it won't be used in the UI
        model.addAttribute("searchField", searchField);
        model.addAttribute("programId", programId);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);
        
        return "admin/Donors/index";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("donor", new DonorCreateDto());
        return "admin/Donors/create";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute DonorCreateDto donorCreateDto) {
        donorsService.create(donorCreateDto);
        return "redirect:/admin/donors";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        DonorResponseDto donor = donorsService.findById(id);
        if (donor == null)
            return "redirect:/admin/donors";
        model.addAttribute("donor", donor);
        return "admin/Donors/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute DonorUpdateDto donorUpdateDto) {
        donorsService.update(id, donorUpdateDto);
        return "redirect:/admin/donors";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        donorsService.delete(id);
        return "redirect:/admin/donors";
    }
    
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable int id, Model model) {
        DonorResponseDto donor = donorsService.findById(id);
        if (donor == null) {
            return "redirect:/admin/donors";
        }
        
        // Get related donation information
        DonationResponseDto donation = donationsService.findById(donor.getDonationId());
        
        // Get program information
        ProgramsResponseDto program = programsService.findById(donor.getProgramId());
        
        // Get user information
        UsersResponseDto user = null;
        if (donor.getUserId() != null) {
            user = usersService.findById(donor.getUserId());
        }
        
        model.addAttribute("donor", donor);
        model.addAttribute("donation", donation);
        model.addAttribute("program", program);
        model.addAttribute("user", user);
        
        return "admin/Donors/detail";
    }
}
