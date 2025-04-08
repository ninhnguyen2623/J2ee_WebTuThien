package com.example.springdonateweb.TestServices;


import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Services.interfaces.IProgramsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

@SpringBootTest
@SpringJUnitWebConfig
public class programTest {
    @Autowired
    private IProgramsService programsService;

    @Test
    public void findAll() {
       List< ProgramsResponseDto> program = programsService.findAll();
       System.out.println(program);

    }

}
