package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Comments.CommentResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Services.interfaces.ICategoriesService;
import com.example.springdonateweb.Services.interfaces.ICommentsService;
import com.example.springdonateweb.Services.interfaces.IDonationsService;
import com.example.springdonateweb.Services.interfaces.IProgramsService;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Controller class for handling admin dashboard related requests
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final IProgramsService programsService;
    private final IDonationsService donationsService;
    private final ICommentsService commentsService;
    private final IUsersService usersService;
    private final ICategoriesService categoriesService;
    
    @GetMapping("")
    public String index(Model model) {
        return dashboard(model);
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // 1. Thống kê tổng quan
            long totalActivePrograms = programsService.findAll().stream().filter(p -> p.isStatus()).count();
            long totalDonors = donationsService.getTopDonors(Integer.MAX_VALUE).size();
            // Sử dụng tổng số tiền từ tất cả các chương trình
            Map<Integer, BigDecimal> programDonations = donationsService.getTotalDonationsByProgram();
            BigDecimal totalDonationAmount = programDonations.values().stream()
                                                             .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // 2. Dữ liệu cho biểu đồ
            Map<String, BigDecimal> donationsByDay = donationsService.getTotalDonationsByDay();
            Map<String, Double> donationsByCategory = categoriesService.getTotalDonationsByCategory();
            
            // 3. Dữ liệu gần đây - Sắp xếp từ mới đến cũ
            // Tạo PageRequest với sắp xếp theo ngày giảm dần (mới nhất lên đầu)
            PageRequest donationsPageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "donationDate"));
            PageRequest commentsPageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
            
            Page<DonationResponseDto> recentDonationsPage = donationsService.findDonationsByPage(donationsPageRequest);
            Page<CommentResponseDto> recentCommentsPage = commentsService.findCommentsByPage(commentsPageRequest);
            
            List<ProgramsResponseDto> allPrograms = programsService.findAll();
            // Sắp xếp chương trình mới nhất lên đầu nếu có trường createdAt hoặc id
            List<ProgramsResponseDto> recentPrograms = allPrograms.subList(0, Math.min(5, allPrograms.size()));
            
            // 4. Thông tin quỹ chung - sử dụng tất cả các chương trình
            List<Map<String, Object>> topDonors = donationsService.getTopDonors(5);
            
            // Thêm dữ liệu vào model
            model.addAttribute("totalActivePrograms", totalActivePrograms);
            model.addAttribute("totalDonors", totalDonors);
            model.addAttribute("totalDonationAmount", totalDonationAmount);
            
            model.addAttribute("donationsByDay", donationsByDay);
            model.addAttribute("donationsByCategory", donationsByCategory);
            
            model.addAttribute("recentDonations", recentDonationsPage.getContent());
            model.addAttribute("recentComments", recentCommentsPage.getContent());
            model.addAttribute("recentPrograms", recentPrograms);
            
            model.addAttribute("allPrograms", allPrograms);
            model.addAttribute("topDonors", topDonors);
        } catch (Exception e) {
            // Log lỗi và bỏ qua
            e.printStackTrace();
        }
        
        return "admin/index";
    }
}