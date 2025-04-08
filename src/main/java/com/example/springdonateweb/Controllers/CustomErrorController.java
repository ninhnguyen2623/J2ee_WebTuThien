package com.example.springdonateweb.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error attributes
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributesMap = errorAttributes.getErrorAttributes(servletWebRequest, ErrorAttributeOptions.defaults());

        // Extract status and error message
        Integer statusCode = (Integer) errorAttributesMap.get("status");
        String errorMessage = (String) errorAttributesMap.get("error");

        // Add attributes to the model for the HTML page
        model.addAttribute("status", statusCode);
        model.addAttribute("message", errorMessage != null ? errorMessage : "Something went wrong!");

        // Determine the view based on status code
        if (statusCode != null) {
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/404";  // Render error/404.html
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errors/500";  // Render error/500.html
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "errors/403";  // Render error/403.html
            }

        }

        return "errors/general";  // Render error/general.html for other cases
    }
}