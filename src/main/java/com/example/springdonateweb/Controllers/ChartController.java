package com.example.springdonateweb.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ChartController {

    @GetMapping("/chart")
    public String showChartPage(Model model) {
        // Nếu bạn có bất kỳ dữ liệu nào cần truyền vào trang, có thể thêm vào model
        // Ví dụ: model.addAttribute("chartData", data);

        // Trả về đường dẫn tới trang Thymeleaf 'admin/Chart/index.html'
        return "admin/Chart/index";  // Đảm bảo rằng tệp 'index.html' nằm trong thư mục src/main/resources/templates/admin/Chart/
    }
}
