package com.example.springdonateweb.TestServices;

import com.example.springdonateweb.Models.Dtos.Users.UserUpdateDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Services.UsersService;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@SpringJUnitWebConfig
public class UserTest {

    @Autowired
    private IUsersService usersService;

    @Test
    public void testFindAll() {
        List<UsersResponseDto> users = usersService.findAll();
        assertNotNull(users);

    }

    @Test
    public void create() {
        UsersResponseDto usersResponseDto = new UsersResponseDto();
    }

    @Test
    public void delete() {
        UsersResponseDto delete = usersService.delete(8);
        if (delete == null) {
            System.out.println("User not found");
        } else {
            System.out.println("User deleted");
        }

    }

    @Test
    public void findByIdAndStatusTrue() {
        UsersResponseDto result = usersService.findByIdAndStatusTrue(8);
        System.out.println(result);

    }

    @Test
    public void existsByEmail() {
        boolean result = usersService.existsByEmail("votrong471@gmail.com");
        if (result) {
            System.out.println("Email exists");
        } else {
            System.out.println("Email not exists");
        }
    }

    @Test
    public void update() {
        UsersResponseDto result = usersService.findByIdAndStatusTrue(9);
        if(result == null) {
            System.out.println("User not found");

        }
        else {
            UserUpdateDto userUpdateDto = new UserUpdateDto();
            userUpdateDto.setId(result.getId());
            userUpdateDto.setName("Vo Trong");
            userUpdateDto.setAddress("Ha Noi");
            userUpdateDto.setPhoneNumber("0987654321");

            if(usersService.update(userUpdateDto) != null) {
                System.out.println("Update success");
            } else {
                System.out.println("Update fail");
            }
        }

    }
}
