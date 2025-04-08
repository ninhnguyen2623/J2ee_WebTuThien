package com.example.springdonateweb.Controllers.client;

import com.example.springdonateweb.Models.Dtos.FundCommon.FundCommonResponseDto;
import com.example.springdonateweb.Services.FundCommonSerivce;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class FundCommonController {

    private final FundCommonSerivce fundCommonSerivce;


    @GetMapping("/fund-common")
    public String fundCommon(Model model) {
        FundCommonResponseDto fundCommon = fundCommonSerivce.findById(1);
        model.addAttribute("fundCommon", fundCommon);
        return "client/fund-common";
    }
}
