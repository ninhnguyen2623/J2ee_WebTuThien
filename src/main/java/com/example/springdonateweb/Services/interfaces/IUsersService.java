package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.Users.UserAddDto;
import com.example.springdonateweb.Models.Dtos.Users.UserCreateDto;
import com.example.springdonateweb.Models.Dtos.Users.UserUpdateDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Models.Entities.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUsersService {
    List<UsersResponseDto> findAll();
    UsersResponseDto findById(int id);
    UsersResponseDto register(UserCreateDto userCreateDto);
    UsersResponseDto update(UserUpdateDto userUpdateDto);
    UsersResponseDto update(UserAddDto userAddDto);
    UsersResponseDto delete(int id);
    boolean existsById(int id);
    void changePassword(int id, String password);
    UsersResponseDto findByEmail(String email);
    UserDetails createUserDetailFromRegister(UsersEntity usersEntity);

    Page<UsersResponseDto> findUsersByPage(int page, int size);

    List<UsersResponseDto> findByStatusTrue();
    UsersResponseDto findByIdAndStatusTrue(int id);
    UsersResponseDto findByEmailAndStatusTrue(String email);

    UsersResponseDto create(UserAddDto userAddDto);
    boolean existsByEmail(String email);
    boolean checkPassword(int id, String password);
    boolean checkComfirmPassword(String password, String confirmPassword);
    void resetPassword(String email);
    void sendOtp(int id, String email);
    void changeEmail(int id);

    boolean checkOtp(int id, String otp);
}
